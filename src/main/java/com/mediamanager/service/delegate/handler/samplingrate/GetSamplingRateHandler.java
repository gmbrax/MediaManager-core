package com.mediamanager.service.delegate.handler.samplingrate;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.SamplingRateMapper;
import com.mediamanager.model.SamplingRate;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.SamplingRateMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.samplingrate.SamplingRateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


@Action("samplingrate.getAll")
public class GetSamplingRateHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(GetSamplingRateHandler.class);

    private final SamplingRateService samplingRateService;

    public GetSamplingRateHandler(SamplingRateService samplingRateService){this.samplingRateService = samplingRateService;}

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload) throws InvalidProtocolBufferException {
        try{
            List<SamplingRate> samplingRates = samplingRateService.getAllSamplingRates();
            SamplingRateMessages.GetSamplingRatesResponse.Builder responseBuilder = SamplingRateMessages.GetSamplingRatesResponse.newBuilder();

            for (SamplingRate samplingRate : samplingRates) {
                SamplingRateMessages.SamplingRate samplingRateProto = SamplingRateMapper.toProtobuf(samplingRate);
                responseBuilder.addSamplingrates(samplingRateProto);
            }
            SamplingRateMessages.GetSamplingRatesResponse getSamplingRatesResponse = responseBuilder.build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(getSamplingRatesResponse.toByteString());

        }catch (Exception e){
            logger.error("Error getting sampling rates", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}
