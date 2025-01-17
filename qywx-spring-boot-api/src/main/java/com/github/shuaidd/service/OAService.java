package com.github.shuaidd.service;

import com.github.shuaidd.dto.checkin.CheckInData;
import com.github.shuaidd.dto.checkin.CheckInRule;
import com.github.shuaidd.dto.tool.DialRecord;
import com.github.shuaidd.exception.WeChatException;
import com.github.shuaidd.response.*;
import com.github.shuaidd.response.oa.*;
import com.github.shuaidd.response.tool.DialRecordResponse;
import com.github.shuaidd.resquest.JournalReportStatRequest;
import com.github.shuaidd.resquest.oa.*;
import com.github.shuaidd.resquest.tool.DialRecordRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 描述 OA
 *
 * @author ddshuai
 * date 2019-04-11 10:15
 **/
@Service
public class OAService extends AbstractBaseService {
    private static final int LIMIT_USER_COUNT = 100;

    /**
     * 获取打卡记录数据
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return CheckInData
     */
    public final List<CheckInData> getCheckInData(CheckInDataRequest request, String applicationName) {
        checkApplication(applicationName);
        checkRequest(request);
        return weChatClient.getCheckInData(request, applicationName).getCheckInDataList();
    }

    private void checkRequest(CheckInDataRequest request) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.getEndTime());
        Objects.requireNonNull(request.getOpenCheckInDataType());
        Objects.requireNonNull(request.getStartTime());
        Objects.requireNonNull(request.getUserIdList());
        if (CollectionUtils.isNotEmpty(request.getUserIdList())) {
            if (request.getUserIdList().size() > LIMIT_USER_COUNT) {
                throw new WeChatException("一次查询人数不能超过100，请分批获取");
            }
        } else {
            throw new WeChatException("查询的人员列表不能为空");
        }
    }

    /**
     * 获取员工打卡规则
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return CheckInRule
     */
    public final List<CheckInRule> getCheckInOption(CheckInRuleRequest request, String applicationName) {
        checkApplication(applicationName);
        List<CheckInRule> checkInRules = new ArrayList<>(1);
        if (Objects.nonNull(request) && Objects.nonNull(request.getDateTime()) && CollectionUtils.isNotEmpty(request.getUserIdList())) {
            if (request.getUserIdList().size() <= LIMIT_USER_COUNT) {
                return weChatClient.getCheckInOption(request, applicationName).getCheckInRules();
            } else {
                throw new WeChatException("一次查询人数不能超过100，请分批获取");
            }
        }

        return checkInRules;
    }

    /**
     * 获取审批数据
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return ApprovalDataResponse
     */
    public final ApprovalDataResponse getApprovalData(ApprovalDataRequest request, String applicationName) {
        checkApplication(applicationName);
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.getEndTime());
        Objects.requireNonNull(request.getStartTime());
        ApprovalDataResponse response = weChatClient.getApprovalData(request, applicationName);
        logger.info("获取审批数据成功：total:{},count:{},next:{}", response.getTotal(), response.getCount(), response.getNextSpNum());
        return response;
    }

    /**
     * 获取公费电话拨打记录
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return DialRecord
     */
    public final List<DialRecord> getDialRecord(DialRecordRequest request, String applicationName) {
        Objects.requireNonNull(request, "参数为空");
        List<DialRecord> records = new ArrayList<>(1);
        return weChatClient.getDialRecord(request, applicationName).getRecords();
    }

    /**
     * 获取企业所有打卡规则
     *
     * @param applicationName 应用名称
     * @return CheckInOptionResponse
     */
    public CheckInOptionResponse getCorpCheckInOption(String applicationName) {
        return weChatClient.getCorpCheckInOption(applicationName);
    }

    /**
     * 获取打卡日报数据
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return CheckInDayReportResponse
     */
    public CheckInDayReportResponse getCheckInDayData(CommonOaRequest request, String applicationName) {
        return weChatClient.getCheckInDayData(request, applicationName);
    }

    /**
     * 获取打卡月报数据
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return CheckInDayReportResponse
     */
    public CheckInDayReportResponse getCheckInMonthData(CommonOaRequest request, String applicationName) {
        return weChatClient.getCheckInMonthData(request, applicationName);
    }

    /**
     * 获取打卡人员排班信息
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return CheckInDayReportResponse
     */
    public CheckInScheduleResponse getCheckInScheduleList(CommonOaRequest request, String applicationName) {
        return weChatClient.getCheckInScheduList(request, applicationName);
    }

    /**
     * 为打卡人员排班
     *
     * @param request         请求
     * @param applicationName 应用名称
     */
    public void setCheckInScheduleList(SetCheckInScheduleRequest request, String applicationName) {
        weChatClient.setCheckInScheduleList(request, applicationName);
    }

    /**
     * 录入打卡人员人脸信息
     *
     * @param request         请求
     * @param applicationName 应用名称
     */
    public void addCheckInUserFace(AddCheckInUserFaceRequest request, String applicationName) {
        weChatClient.addCheckInUserFace(request, applicationName);
    }

    /**
     * 获取审批模板详情
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return ApproveTemplateResponse
     */
    public ApproveTemplateResponse getTemplateDetail(TemplateRequest request, String applicationName) {
        return weChatClient.getTemplateDetail(request, applicationName);
    }

    /**
     * 提交审批申请
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return ApplyEventResponse
     */
    public ApplyEventResponse applyEvent(ApplyEventRequest request, String applicationName) {
        return weChatClient.applyEvent(request, applicationName);
    }

    /**
     * 批量获取审批单号
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return SpNoResponse
     */
    public SpNoResponse getApprovalInfo(GetApprovalNoRequest request, String applicationName) {
        return weChatClient.getApprovalInfo(request, applicationName);
    }

    /**
     * 获取审批申请详情
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return ApprovalDetailResponse
     */
    public ApprovalDetailResponse getApprovalDetail(ApprovalDetailRequest request, String applicationName) {
        return weChatClient.getApprovalDetail(request, applicationName);
    }

    /**
     * 获取企业假期管理配置
     *
     * @param applicationName 应用名称
     * @return VacationConfigResponse
     */
    public VacationConfigResponse getCorpConf(String applicationName) {
        return weChatClient.getCorpConf(applicationName);
    }

    /**
     * 获取成员假期余额
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return UserVacationQuotaResponse
     */
    public UserVacationQuotaResponse getUserVacationQuota(UserIdRequest request, String applicationName) {
        return weChatClient.getUserVacationQuota(request, applicationName);
    }

    /**
     * 修改成员假期余额
     *
     * @param request         请求
     * @param applicationName 应用名称
     */
    public void setOneUserQuota(UpdateUserQuotaRequest request, String applicationName) {
        weChatClient.setOneUserQuota(request, applicationName);
    }

    /**
     * 批量获取汇报记录单号
     *
     * @param recordRequest   请求
     * @param applicationName 应用名称
     * @return UserVacationQuotaResponse
     */
    public JournalRecordResponse getJournalRecordList(JournalRecordRequest recordRequest, String applicationName) {
        return weChatClient.getJournalRecordList(recordRequest, applicationName);
    }

    /**
     * 获取汇报记录详情
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return UserVacationQuotaResponse
     */
    public JournalReportDetailResponse getJournalRecordDetail(JournalReportDetailRequest request, String applicationName) {
        return weChatClient.getJournalRecordDetail(request, applicationName);
    }

    /**
     * 获取汇报统计数据
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return UserVacationQuotaResponse
     */
    public JournalReportStatResponse getJournalStatList(JournalReportStatRequest request, String applicationName) {
        return weChatClient.getJournalStatList(request, applicationName);
    }

    /**
     * 添加会议室
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return AddMeetingRoomResponse
     */
    public AddMeetingRoomResponse addMeetingRoom(MeetingRoomRequest request, String applicationName) {
        return weChatClient.addMeetingRoom(request, applicationName);
    }

    /**
     * 编辑会议室
     *
     * @param request         请求
     * @param applicationName 应用名称
     */
    public void updateMeetingRoom(MeetingRoomRequest request, String applicationName) {
        weChatClient.updateMeetingRoom(request, applicationName);
    }

    /**
     * 删除会议室
     *
     * @param request         请求
     * @param applicationName 应用名称
     */
    public void delMeetingRoom(MeetingRoomRequest request, String applicationName) {
        weChatClient.delMeetingRoom(request, applicationName);
    }

    /**
     * 查询会议室
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return MeetingRoomListResponse
     */
    public MeetingRoomListResponse searchMeetingRoom(MeetingRoomRequest request, String applicationName) {
        return weChatClient.searchMeetingRoom(request, applicationName);
    }

    /**
     * 查询会议室的预定信息
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return BookingInfoResponse
     */
    public BookingInfoResponse getBookingInfo(GetBookingInfoRequest request, String applicationName) {
        return weChatClient.getBookingInfo(request, applicationName);
    }

    /**
     * 预定会议室
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return BookMeetingRoomResponse
     */
    public BookMeetingRoomResponse bookMeetingRoom(BookMeetingRoomRequest request, String applicationName) {
        return weChatClient.bookMeetingRoom(request, applicationName);
    }

    /**
     * 取消预定会议室
     *
     * @param request         请求
     * @param applicationName 应用名称
     */
    public void cancelBookMeetingRoom(CancelBookRequest request, String applicationName) {
        weChatClient.cancelBookMeetingRoom(request, applicationName);
    }

    /**
     * 发起语音电话
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return EmergencyCallResponse
     */
    public EmergencyCallResponse pstnccCall(EmergencyCallRequest request, String applicationName) {
        return weChatClient.pstnccCall(request, applicationName);
    }

    /**
     * 获取接听状态
     *
     * @param request         请求
     * @param applicationName 应用名称
     * @return GetCallStateResponse
     */
    public GetCallStateResponse pstnccCallState(GetCallStateRequest request, String applicationName) {
        return weChatClient.pstnccCallState(request, applicationName);
    }
}
