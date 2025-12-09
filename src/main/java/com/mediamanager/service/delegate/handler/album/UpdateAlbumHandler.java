package com.mediamanager.service.delegate.handler.album;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mediamanager.mapper.AlbumMapper;
import com.mediamanager.model.Album;
import com.mediamanager.protocol.TransportProtocol;
import com.mediamanager.protocol.messages.AlbumMessages;
import com.mediamanager.service.delegate.ActionHandler;
import com.mediamanager.service.delegate.annotation.Action;
import com.mediamanager.service.album.AlbumService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Action("album.update")
public class UpdateAlbumHandler implements ActionHandler {
    private static final Logger logger = LogManager.getLogger(UpdateAlbumHandler.class);
    private final AlbumService albumService;

    public UpdateAlbumHandler(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Override
    public TransportProtocol.Response.Builder handle(ByteString requestPayload)
            throws InvalidProtocolBufferException {
        try {
            AlbumMessages.UpdateAlbumRequest updateRequest =
                    AlbumMessages.UpdateAlbumRequest.parseFrom(requestPayload);

            int id = updateRequest.getId();

            // Extrai valores dos wrappers - null se não foi fornecido
            Integer year = updateRequest.hasYear()
                    ? updateRequest.getYear().getValue()
                    : null;

            Integer numberOfDiscs = updateRequest.hasNumberOfDiscs()
                    ? updateRequest.getNumberOfDiscs().getValue()
                    : null;

            Integer albumTypeId = updateRequest.hasFkAlbumtypeId()
                    ? updateRequest.getFkAlbumtypeId().getValue()
                    : null;

            Integer albumArtId = updateRequest.hasFkAlbumartId()
                    ? updateRequest.getFkAlbumartId().getValue()
                    : null;

            Optional<Album> albumOpt = albumService.updateAlbum(
                    id,
                    updateRequest.getName(),
                    year,
                    numberOfDiscs,
                    updateRequest.getCode().isEmpty() ? null : updateRequest.getCode(),
                    updateRequest.getIsCompilation(),
                    albumTypeId,
                    albumArtId,
                    updateRequest.hasYear(),              // ← Novo!
                    updateRequest.hasNumberOfDiscs(),     // ← Novo!
                    updateRequest.hasFkAlbumtypeId(),     // ← Novo!
                    updateRequest.hasFkAlbumartId()       // ← Novo!
            );

            if (albumOpt.isEmpty()) {
                logger.warn("Album not found with ID: {}", id);
                return TransportProtocol.Response.newBuilder()
                        .setStatusCode(404)
                        .setPayload(ByteString.copyFromUtf8("Album not found"));
            }

            AlbumMessages.Album albumProto = AlbumMapper.toProtobuf(albumOpt.get());

            AlbumMessages.UpdateAlbumResponse updateResponse =
                    AlbumMessages.UpdateAlbumResponse.newBuilder()
                            .setAlbum(albumProto)
                            .build();

            return TransportProtocol.Response.newBuilder()
                    .setPayload(updateResponse.toByteString());

        } catch (IllegalArgumentException e) {
            logger.error("Validation error", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(400)
                    .setPayload(ByteString.copyFromUtf8("Validation error: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating album", e);
            return TransportProtocol.Response.newBuilder()
                    .setStatusCode(500)
                    .setPayload(ByteString.copyFromUtf8("Error: " + e.getMessage()));
        }
    }
}