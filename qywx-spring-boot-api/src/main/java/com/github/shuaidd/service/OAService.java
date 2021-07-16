package com.github.shuaidd.service;

import com.github.shuaidd.dto.checkin.CheckInData;
import com.github.shuaidd.dto.checkin.CheckInRule;
import com.github.shuaidd.dto.tool.DialRecord;
import com.github.shuaidd.exception.WeChatException;
import com.github.shuaidd.response.*;
import com.github.shuaidd.response.oa.*;
import com.github.shuaidd.response.tool.DialRecordResponse;
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
     * @return  ApproveTemplateResponse
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
        return weChatClient.applyEvent(request,applicationName);
    }
}
