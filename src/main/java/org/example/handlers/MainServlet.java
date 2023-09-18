package org.example.handlers;

import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.example.exception.ProcessException;
import org.example.model.*;
import org.example.utils.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeoutException;

public class MainServlet extends HttpServlet
{
    private static Logger log = LoggerFactory.getLogger(MainServlet.class.getSimpleName());
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String body = IOUtils.toString(req.getInputStream());
        if(StringUtils.isBlank(body))
        {
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(Common.getPrettyGson().toJson(new ClientTransferResponse("404", "body is blank")));
            return;
        }
        ClientTransferRequest r = Common.getPrettyGson().fromJson(body, ClientTransferRequest.class);
        PersonInfoResponse personInfoResponse = new PersonInfoResponse();
        try {
            personInfoResponse = requestPersonInfoService(new PersonInfoRequest(r.getFromPhone(), r.getToPhone()));
        } catch (ProcessException e) {
            resp.setContentType("application/json");
            resp.setStatus(e.getStatus());
            ErrorDto errorDto = new ErrorDto(e.getCode(), e.getDescription());
            resp.getWriter().println(Common.getPrettyGson().toJson(errorDto));
            return;
        }
        ResponseInTransferMoney responseInTransferMoney = null;
        try {
            responseInTransferMoney = new ResponseInTransferMoney(personInfoResponse.getFromPersonId().getPersonId(), personInfoResponse.getToPersonId().getPersonId(), r.getAmount());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            resp.getWriter().println(Common.getPrettyGson().toJson(new ClientTransferResponse("400", "повторите запрос позже, превышенно ожидание")));
            return;
        }
        PaymentsResult result = new PaymentsResult();
        try {
            result = requestTransferMoneyService(responseInTransferMoney);
        } catch (ProcessException e) {
            resp.setContentType("application/json");
            resp.setStatus(e.getStatus());
            ErrorDto errorDto = new ErrorDto(e.getCode(), e.getDescription());
            resp.getWriter().println(Common.getPrettyGson().toJson(errorDto));
            return;
        }

        PaymentsResult paymentsResult = new PaymentsResult(result.getAmount(), result.getStatus(), result.getAccount_from(), result.getAccount_to());
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(Common.getPrettyGson().toJson(paymentsResult));
    }

    private PersonInfoResponse requestPersonInfoService(PersonInfoRequest personInfoRequest) throws IOException, ProcessException {
        String url = "http://localhost:8081/get/info";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);


        StringEntity entity = new StringEntity(Common.getPrettyGson().toJson(personInfoRequest));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);

        HttpEntity resp = response.getEntity();
        String respStr = IOUtils.toString(resp.getContent());

        return Common.getPrettyGson().fromJson(respStr, PersonInfoResponse.class);
    }


    private PaymentsResult  requestTransferMoneyService(ResponseInTransferMoney responseInTransferMoney) throws IOException, ProcessException {
        String url = "http://localhost:8082/do/transfer";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);


        StringEntity entity = new StringEntity(Common.getPrettyGson().toJson(responseInTransferMoney));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 200) {
            HttpEntity resp = response.getEntity();
            String respStr = IOUtils.toString(resp.getContent());
            return Common.getPrettyGson().fromJson(respStr, PaymentsResult.class);
        }

        ErrorDto errorDto = new ErrorDto();
        try {
            HttpEntity resp = response.getEntity();
            String respStr = IOUtils.toString(resp.getContent());
            errorDto = Common.getPrettyGson().fromJson(respStr, ErrorDto.class);
        } catch (IOException | UnsupportedOperationException | JsonSyntaxException e) {
            throw new ProcessException("400", "Не обаботался платеж, повторите запрос чуть поже", HttpServletResponse.SC_BAD_REQUEST);
        }
        throw new ProcessException(errorDto.getCode(), errorDto.getDescription(), statusCode);
    }


}
