package com.nsu.csd.data.remote;

import com.nsu.csd.model.EventDTO;
import com.nsu.csd.model.MeetingInfoDto;
import com.nsu.csd.model.MeetingSummaryDto;
import com.nsu.csd.model.NewEventDTO;
import com.nsu.csd.model.NewMeetingDto;
import com.nsu.csd.model.TokenDTO;
import com.nsu.csd.model.EventSummaryWithIdDTO;
import com.nsu.csd.model.UserLoginDTO;
import com.nsu.csd.model.UserRegistrationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * аннотация - на какой запрос отвечаем @GET("/api/get")
 */
public interface Api {

    @POST("sign_up")
    Call<Void> sign_up(@Body UserRegistrationDTO userRegistrationDTO);

    @POST("sign_in")
    Call<TokenDTO> sign_in(@Body UserLoginDTO userLoginDTO);

    @POST("logout")
    Call<Void> logout(@Body Object o);

    @GET("events/day/{year}/{month}/{day}")
    Call<List<EventSummaryWithIdDTO>> get_ev_day(@Path("year") String year,@Path("month") String month,@Path("day") String day);

    @POST("events")
    Call<Void> events(@Body NewEventDTO o);

    @GET("events/{id}")
    Call<EventDTO> get_event_full(@Path("id") String id);

    @DELETE("events/{id}")
    Call<Void> delete_event(@Path("id") String id);

    @PUT("events")
    Call<Void> update_event(@Body EventDTO o);

    @GET("meetings")
    Call<List<MeetingSummaryDto>> get_meet_list();

    @POST("meetings")
    Call<Void> save_meet(@Body NewMeetingDto o);

    @GET("meetings/{id}")
    Call<MeetingInfoDto> get_month_full(@Path("id") String id);

    @POST("meetings/{meetingId}/actions/joining")
    Call<Void> add_to_meet(@Path("meetingId") String id);

    @POST("meetings/{meetingId}/actions/leaving")
    Call<Void> leave_meet(@Path("meetingId") String id);

}
