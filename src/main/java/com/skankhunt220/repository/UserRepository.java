package com.skankhunt220.repository;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.google.gson.JsonParser;
import com.skankhunt220.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final Sheets sheetsService;

    private String getSheetId(String id, Sheets sheetsService, String spreadsheetId) throws IOException {
        ValueRange sendId = new ValueRange().setValues(Arrays.asList(Arrays.asList("=MATCH(\"" + id + "\"; usersList!A:A; 0)")));
        String updatedData = sheetsService.spreadsheets().values()
                .update(spreadsheetId, "getUserSheet!A2", sendId)
                .setIncludeValuesInResponse(true)
                .setResponseValueRenderOption("UNFORMATTED_VALUE")
                .setValueInputOption("USER_ENTERED")
                .execute().get("updatedData").toString();

        return new JsonParser().parse(updatedData).getAsJsonObject().get("values").getAsString();
    }

    public User save(User user, String spreadsheetId) throws IOException {
        user.setId(UUID.randomUUID().toString());
        ValueRange appendUser = new ValueRange().setValues(Arrays.asList(
                Arrays.asList(user.getId(), user.getFirstName(), user.getMiddleName(), user.getLastName())
        ));
        sheetsService.spreadsheets().values()
                .append(spreadsheetId, "usersList", appendUser)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();
        return user;
    }

    public Page<User> findAll(Pageable pageable, String spreadsheetId) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, "usersList!A2:D")
                .execute();
        List<User> users = new ArrayList<>();
        List<List<Object>> values = response.getValues();

        for (List<Object> row : values)
            users.add(new User(
                    row.get(0).toString(),
                    row.get(1).toString(),
                    row.get(2).toString(),
                    row.get(3).toString()
            ));

        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > users.size() ? users.size() : (start + pageable.getPageSize()));
        return new PageImpl<User>(users.subList(start, end), pageable, users.size());
    }

    public User findById(String id, String spreadsheetId) throws IOException {
        String sheetId = getSheetId(id, sheetsService, spreadsheetId);
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, "usersList!A" + sheetId + ":D" + sheetId)
                .execute();
        List<Object> dataFromSheet = response.getValues().get(0);

        return new User(dataFromSheet.get(0).toString(),
                dataFromSheet.get(1).toString(),
                dataFromSheet.get(2).toString(),
                dataFromSheet.get(3).toString()
        );
    }

    public User update(String id, User user, String spreadsheetId) throws IOException {
        String sheetId = getSheetId(id, sheetsService, spreadsheetId);

        ValueRange userFieldsUpdate = new ValueRange().setValues(Arrays.asList(
                Arrays.asList(user.getFirstName(), user.getMiddleName(), user.getLastName())
        ));
        sheetsService.spreadsheets().values()
                .update(spreadsheetId, "B" + sheetId, userFieldsUpdate)
                .setValueInputOption("USER_ENTERED").execute();

        return new User(id, user.getFirstName(), user.getMiddleName(), user.getLastName());
    }

    public void deleteById(String id, String spreadsheetId) throws IOException {
        int sheetId = Integer.parseInt(getSheetId(id, sheetsService, spreadsheetId));

        DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest()
                .setRange(
                        new DimensionRange().setSheetId(0)
                                .setDimension("ROWS")
                                .setStartIndex(sheetId - 1)
                                .setEndIndex(sheetId)
                );
        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setDeleteDimension(deleteRequest));

        sheetsService.spreadsheets().batchUpdate(spreadsheetId, new BatchUpdateSpreadsheetRequest().setRequests(requests)).execute();
    }
}