package com.arcltd.staff.networkhandler.remote;


import com.arcltd.staff.response.AddConveyanceMobileResponse;
import com.arcltd.staff.response.AddCustomerDeatilsResponse;
import com.arcltd.staff.response.AddFaildVehicleResponse;
import com.arcltd.staff.response.AddMessResponse;
import com.arcltd.staff.response.AddMessageResponse;
import com.arcltd.staff.response.AddRemarkResponse;
import com.arcltd.staff.response.AddSweeperPeonResponse;
import com.arcltd.staff.response.AddTeeElectricityResponse;
import com.arcltd.staff.response.AddUpdatWeightMachineResponse;
import com.arcltd.staff.response.AddUpdateDailyBookingResponse;
import com.arcltd.staff.response.AddUpdateEmployeeResponse;
import com.arcltd.staff.response.AddUpdateMoterCycleResponse;
import com.arcltd.staff.response.AddUpdatedAdminiExpResponse;
import com.arcltd.staff.response.AdministrativEXPAdminResponse;
import com.arcltd.staff.response.AdvEXPResponse;
import com.arcltd.staff.response.AppPermissionListResponse;
import com.arcltd.staff.response.BranchListPiccodeResponse;
import com.arcltd.staff.response.BranchListResponse;
import com.arcltd.staff.response.CheckStatusResponse;
import com.arcltd.staff.response.ConveyanceMobileListResponse;
import com.arcltd.staff.response.CrashReportListResponse;
import com.arcltd.staff.response.CrashReportREsponse;
import com.arcltd.staff.response.CustomerListResponse;
import com.arcltd.staff.response.DeleteEmployeeResponse;
import com.arcltd.staff.response.DeleteResponse;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.response.EmpMessageListResponse;
import com.arcltd.staff.response.EmpRemarkListResponse;
import com.arcltd.staff.response.EmployeeActive_InctiveResponse;
import com.arcltd.staff.response.EmployeeListResponse;
import com.arcltd.staff.response.EmployeeSignupDetailsResponse;
import com.arcltd.staff.response.EmployeeTransferHistoryResponse;
import com.arcltd.staff.response.FeedbackListResponse;
import com.arcltd.staff.response.FeedbackResponse;
import com.arcltd.staff.response.FinalSignUpResponseResponse;
import com.arcltd.staff.response.ForgotPassResponse;
import com.arcltd.staff.response.GetOTPResponseResponse;
import com.arcltd.staff.response.InchargeListResponse;
import com.arcltd.staff.response.InsAmoutResponse;
import com.arcltd.staff.response.InsCompanyResponse;
import com.arcltd.staff.response.LandlordGodownListResponse;
import com.arcltd.staff.response.LandlordUpdateResponse;
import com.arcltd.staff.response.LoginRespponse;
import com.arcltd.staff.response.MessEmpListResponse;
import com.arcltd.staff.response.MessListResponse;
import com.arcltd.staff.response.MoterCycleListResponse;
import com.arcltd.staff.response.OfficeGodownMessListResponse;
import com.arcltd.staff.response.ProfilePictureUpdateResponse;
import com.arcltd.staff.response.ProfileResponseResponse;
import com.arcltd.staff.response.RegionListResponse;
import com.arcltd.staff.response.RegisterRespponse;
import com.arcltd.staff.response.RemarkListResponse;
import com.arcltd.staff.response.SendDiviceTokenResponse;
import com.arcltd.staff.response.SoldMoterCycleResponse;
import com.arcltd.staff.response.SweeperPeonListResponse;
import com.arcltd.staff.response.TargetResponseResponse;
import com.arcltd.staff.response.TransferEmployeeResponse;
import com.arcltd.staff.response.UpdatePassResponse;
import com.arcltd.staff.response.UpdatePermissionResponse;
import com.arcltd.staff.response.UserListResponse;
import com.arcltd.staff.response.VehicleFailListResponse;
import com.arcltd.staff.response.WeightMachineListResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebService {


    @FormUrlEncoded
    @POST("user_login.php")
    Single<LoginRespponse> login(@Field(value = "email", encoded = true) String email,
                                 @Field(value = "password", encoded = true) String password);

    @FormUrlEncoded
    @POST("updatePassword.php")
    Single<UpdatePassResponse> resetPass(@Field(value = "email", encoded = true) String email,
                                         @Field(value = "current", encoded = true) String current,
                                         @Field(value = "new", encoded = true) String newPass);


    @FormUrlEncoded
    @POST("user_registration.php")
    Single<RegisterRespponse> signup(
            @Field(value = "name", encoded = true) String name,
            @Field(value = "email", encoded = true) String email,
            @Field(value = "password", encoded = true) String password,
            @Field(value = "contactno", encoded = true) String contactno,
            @Field(value = "login_type", encoded = true) String login_type,
            @Field(value = "region_id", encoded = true) String region_id,
            @Field(value = "region_name", encoded = true) String region_name,
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "division_name", encoded = true) String division_name,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "branch_name", encoded = true) String branch_name,
            @Field(value = "emp_code", encoded = true) String emp_code,
            @Field(value = "design", encoded = true) String design,
            @Field(value = "status", encoded = true) String status,
            @Field(value = "mess_name", encoded = true) String mess_name,
            @Field(value = "vari_status", encoded = true) String vari_status);

    @FormUrlEncoded
    @POST("app_PermissionList.php")
    Single<AppPermissionListResponse> list_userPermission(@Field(value = "region_id", encoded = true) String region_id,
                                       @Field(value = "search", encoded = true) String search);

    @FormUrlEncoded
    @POST("crash_reportList.php")
    Single<CrashReportListResponse> list_crash(@Field(value = "region_id", encoded = true) String region_id,
                                               @Field(value = "search", encoded = true) String search);


    @FormUrlEncoded
    @POST("autoAddCrashReport.php")
    Single<CrashReportREsponse> getCrashData(@Field(value = "report", encoded = true) String report,
                                             @Field(value = "type", encoded = true) String type);


    @FormUrlEncoded
    @POST("app_PermissionUpdate.php")
    Single<UpdatePermissionResponse> appPermission(
            @Field(value = "region_id", encoded = true) String region_id,
            @Field(value = "emp_code", encoded = true) String emp_code,
            @Field(value = "div_branches", encoded = true) String div_branches,
            @Field(value = "div_mess", encoded = true) String div_mess,
            @Field(value = "send_profile", encoded = true) String send_profile,
            @Field(value = "emp_list", encoded = true) String emp_list,
            @Field(value = "emp_birthday", encoded = true) String emp_birthday,
            @Field(value = "retire_list", encoded = true) String retire_list,
            @Field(value = "motercy_list", encoded = true) String motercy_list,
            @Field(value = "inciden_list", encoded = true) String inciden_list,
            @Field(value = "weightmc_list", encoded = true) String weightmc_list,
            @Field(value = "conveyance", encoded = true) String conveyance,
            @Field(value = "lanlord_details", encoded = true) String lanlord_details,
            @Field(value = "admin_exp_list", encoded = true) String admin_exp_list,
            @Field(value = "sendEmp_message", encoded = true) String sendEmp_message,
            @Field(value = "add_mess", encoded = true) String add_mess,
            @Field(value = "add_emp", encoded = true) String add_emp,
            @Field(value = "add_convence_mobExp", encoded = true) String add_convence_mobExp,
            @Field(value = "add_sweeperPeon", encoded = true) String add_sweeperPeon,
            @Field(value = "transfer_emp", encoded = true) String transfer_emp,
            @Field(value = "active_deactiveEmp", encoded = true) String active_deactiveEmp);



    @Multipart
    @POST("updateEmployee.php")
    Single<ProfilePictureUpdateResponse> getUpPic(
            @Path(value = "email", encoded = true) String email,
            @Field(value = "cpassword", encoded = true) String cpassword);


    @FormUrlEncoded
    @POST("sendPasswordResetCode.php")
    Single<GetOTPResponseResponse> getOTP(
            @Field(value = "email", encoded = true) String email);

    @FormUrlEncoded
    @POST("submitForgotPassCode.php")
    Single<ForgotPassResponse> getForgot(
            @Field(value = "email", encoded = true) String email,
            @Field(value = "password", encoded = true) String password,
            @Field(value = "cpassword", encoded = true) String cpassword);


    @FormUrlEncoded
    @POST("userSubmitOTP.php")
    Single<FinalSignUpResponseResponse> SubmitOtp(
            @Field(value = "email", encoded = true) String email,
            @Field(value = "code", encoded = true) String code,
            @Field(value = "status", encoded = true) String status,
            @Field(value = "vari_status", encoded = true) String vari_status);


    @FormUrlEncoded
    @POST("addEmployeeDetails.php")
    Single<AddUpdateEmployeeResponse> addEmployee(
            @Field(value = "region_name", encoded = true) String region_name,
            @Field(value = "region_id", encoded = true) String region_id,
            @Field(value = "division_name", encoded = true) String division_name,
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "branch_name", encoded = true) String branch_name,
            @Field(value = "emp_act_retire", encoded = true) String emp_act_retire,
            @Field(value = "emp_code", encoded = true) String emp_code,
            @Field(value = "emp_name", encoded = true) String emp_name,
            @Field(value = "emp_eqalification", encoded = true) String emp_eqalification,
            @Field(value = "emp_joining_date", encoded = true) String emp_joining_date,
            @Field(value = "emp_desig", encoded = true) String emp_desig,
            @Field(value = "emp_salary", encoded = true) String emp_salary,
            @Field(value = "status", encoded = true) String status,
            @Field(value = "dat_of_birth", encoded = true) String dat_of_birth,
            @Field(value = "uan_no", encoded = true) String uan_no);

    @FormUrlEncoded
    @POST("forgotPass.php")
    Single<ForgotPassResponse> forgotPass(
            @Field(value = "email", encoded = true) String email);

    @GET("region_list.php")
    Single<RegionListResponse> list_region();

    @FormUrlEncoded
    @POST("division_list.php")
    Single<DivisionListResponse> list_division(@Field(value = "region_id", encoded = true) String region_id);

    @FormUrlEncoded
    @POST("inchargeList.php")
    Single<InchargeListResponse> list_Incharges(@Field(value = "region_id", encoded = true) String region_id);

    @FormUrlEncoded
    @POST("employeeMessageList.php")
    Single<EmpMessageListResponse> list_message(@Field(value = "emp_code", encoded = true) String emp_code);

    @FormUrlEncoded
    @POST("employeeListSignup.php")
    Single<EmployeeSignupDetailsResponse> list_empSignoup
            (@Field(value = "emp_code", encoded = true) String emp_code);

    @FormUrlEncoded
    @POST("deleteAllEmployee.php")
    Single<DeleteEmployeeResponse> deleteEmployee(@Field(value = "region_id", encoded = true) String region_id);

    @FormUrlEncoded
    @POST("messEmployeeList.php")
    Single<MessEmpListResponse> messEmployeeList(@Field(value = "mes_id", encoded = true) String mes_id);


    @FormUrlEncoded
    @POST("employee_list.php")
    Single<EmployeeListResponse> list_emp(
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "search", encoded = true) String search);

    @FormUrlEncoded
    @POST("todayEmployeeBirthdayList.php")
    Single<EmployeeListResponse> listBirth_emp(
            @Field(value = "region_id", encoded = true) String region_id,
            @Field(value = "search", encoded = true) String search);

    @FormUrlEncoded
    @POST("retireEmployeeList.php")
    Single<EmployeeListResponse> retireList_emp(
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "search", encoded = true) String search);

    @FormUrlEncoded
    @POST("office_mess_rent.php")
    Single<OfficeGodownMessListResponse>
    list_ofiicemessgdn(@Field(value = "division_id", encoded = true) String division_id,
                       @Field(value = "search", encoded = true) String search);

    @FormUrlEncoded
    @POST("target.php")
    Single<TargetResponseResponse>
    target(@Field(value = "branch_code", encoded = true) String branch_code,
           @Field(value = "search", encoded = true) String search);

    @FormUrlEncoded
    @POST("mess_list.php")
    Single<MessListResponse>
    list_mess(@Field(value = "div_name", encoded = true) String div_name);

    @FormUrlEncoded
    @POST("branch_list.php")
    Single<BranchListResponse>
    list_branch(@Field(value = "branch_code", encoded = true) String branch_code,
                @Field(value = "division_id", encoded = true) String division_id);

    @FormUrlEncoded
    @POST("branchlist_pincode.php")
    Single<BranchListPiccodeResponse>
    list_branchPincodeWise(@Field(value = "branch_code", encoded = true) String branch_code,
                @Field(value = "division_id", encoded = true) String division_id);

    @FormUrlEncoded
    @POST("addMess.php")
    Single<AddMessResponse> addMESS(
            @Field(value = "reg_id", encoded = true) String reg_id,
            @Field(value = "mess_name", encoded = true) String mess_name,
            @Field(value = "mess_address", encoded = true) String mess_address,
            @Field(value = "cook_salary", encoded = true) String cook_salary,
            @Field(value = "div_name", encoded = true) String div_name,
            @Field(value = "own_rented", encoded = true) String own_rented,
            @Field(value = "cook_avl", encoded = true) String cook_avl);


    @FormUrlEncoded
    @POST("weight_mc_list.php")
    Single<EmployeeListResponse> list_weight(@Field(value = "branch_code", encoded = true) String branch_code);

    @FormUrlEncoded
    @POST("motercycle_list.php")
    Single<EmployeeListResponse> list_moter_cycle(@Field(value = "branch_code", encoded = true) String branch_code);

    @FormUrlEncoded
    @POST("branch_exp_list.php")
    Single<EmployeeListResponse> list_branchExp(@Field(value = "branch_code", encoded = true) String branch_code);

    @FormUrlEncoded
    @POST("admistration_exp_list.php")
    Single<AdvEXPResponse> administrati_exp(@Field(value = "branch_code", encoded = true) String branch_code);

    @FormUrlEncoded
    @POST("addupdate_weightmachine.php")
    Single<AddMessResponse> addWeightMachine(
            @Field(value = "region_id", encoded = true) String region_id,
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "weightmc_no", encoded = true) String weightmc_no,
            @Field(value = "make", encoded = true) String make,
            @Field(value = "model", encoded = true) String model,
            @Field(value = "renue_uoto", encoded = true) String renue_uoto);

    @FormUrlEncoded
    @POST("addupdate_motercycle.php")
    Single<AddMessResponse> addMoterCycle(
            @Field(value = "region_id", encoded = true) String region_id,
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "vehicle_no", encoded = true) String vehicle_no,
            @Field(value = "registation_no", encoded = true) String registation_no,
            @Field(value = "make", encoded = true) String make,
            @Field(value = "model", encoded = true) String model,
            @Field(value = "insurance_upto", encoded = true) String insurance_upto,
            @Field(value = "puc_upto", encoded = true) String puc_upto);

    @FormUrlEncoded
    @POST("addbranch_expencess.php")
    Single<AddMessResponse> addBranchExp(
            @Field(value = "region_id", encoded = true) String region_id,
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "sweeper", encoded = true) String sweeper,
            @Field(value = "peon", encoded = true) String peon,
            @Field(value = "tea", encoded = true) String tea,
            @Field(value = "electricty", encoded = true) String electricty);

    @FormUrlEncoded
    @POST("addupdate_administrativeexp.php")
    Single<AddUpdatedAdminiExpResponse> addupdateAdminExp(
            @Field(value = "region_id", encoded = true) String region_id,
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "telephone", encoded = true) String telephone,
            @Field(value = "pastge_courier", encoded = true) String pastge_courier,
            @Field(value = "print_stationery", encoded = true) String print_stationery,
            @Field(value = "bike_petrol_exp", encoded = true) String bike_petrol_exp,
            @Field(value = "bike_maitainance", encoded = true) String bike_maitainance,
            @Field(value = "car_petrol_exp", encoded = true) String car_petrol_exp,
            @Field(value = "car_maitainance", encoded = true) String car_maitainance,
            @Field(value = "conveyance", encoded = true) String conveyance,
            @Field(value = "travelling", encoded = true) String travelling,
            @Field(value = "staff_welfare", encoded = true) String staff_welfare,
            @Field(value = "misc_charges", encoded = true) String misc_charges,
            @Field(value = "busi_pramostion", encoded = true) String busi_pramostion,
            @Field(value = "office_maitanance", encoded = true) String office_maitanance,
            @Field(value = "office_eqp_maintenance", encoded = true) String office_eqp_maintenance,
            @Field(value = "repare_buildings", encoded = true) String repare_buildings,
            @Field(value = "salary_bonas_gradu_other", encoded = true) String salary_bonas_gradu_other,
            @Field(value = "cocontribution_pf_esi", encoded = true) String cocontribution_pf_esi,
            @Field(value = "rent_paid", encoded = true) String rent_paid,
            @Field(value = "electricity", encoded = true) String electricity,
            @Field(value = "books_periodicals", encoded = true) String books_periodicals,
            @Field(value = "advertisement", encoded = true) String advertisement,
            @Field(value = "recrument_place_conf", encoded = true) String recrument_place_conf,
            @Field(value = "insurance", encoded = true) String insurance,
            @Field(value = "legal", encoded = true) String legal,
            @Field(value = "subscription", encoded = true) String subscription,
            @Field(value = "charity_donation", encoded = true) String charity_donation,
            @Field(value = "auditor_consultancy", encoded = true) String auditor_consultancy,
            @Field(value = "rates_taxes", encoded = true) String rates_taxes,
            @Field(value = "bank_commission", encoded = true) String bank_commission,
            @Field(value = "emp_children_education", encoded = true) String emp_children_education,
            @Field(value = "status", encoded = true) String status);

    @FormUrlEncoded
    @POST("administrativeExpListAdmin.php")
    Single<AdministrativEXPAdminResponse> list_administrative
            (@Field(value = "region_id", encoded = true) String region_id);


    @FormUrlEncoded
    @POST("finalAdministrativeexpenses.php")
    Single<AddUpdatedAdminiExpResponse> finaladdupdateAdminExp(
            @Field(value = "region_id", encoded = true) String region_id,
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "telephone", encoded = true) String telephone,
            @Field(value = "pastge_courier", encoded = true) String pastge_courier,
            @Field(value = "print_stationery", encoded = true) String print_stationery,
            @Field(value = "bike_petrol_exp", encoded = true) String bike_petrol_exp,
            @Field(value = "bike_maitainance", encoded = true) String bike_maitainance,
            @Field(value = "car_petrol_exp", encoded = true) String car_petrol_exp,
            @Field(value = "car_maitainance", encoded = true) String car_maitainance,
            @Field(value = "conveyance", encoded = true) String conveyance,
            @Field(value = "travelling", encoded = true) String travelling,
            @Field(value = "staff_welfare", encoded = true) String staff_welfare,
            @Field(value = "misc_charges", encoded = true) String misc_charges,
            @Field(value = "busi_pramostion", encoded = true) String busi_pramostion,
            @Field(value = "office_maitanance", encoded = true) String office_maitanance,
            @Field(value = "office_eqp_maintenance", encoded = true) String office_eqp_maintenance,
            @Field(value = "repare_buildings", encoded = true) String repare_buildings,
            @Field(value = "salary_bonas_gradu_other", encoded = true) String salary_bonas_gradu_other,
            @Field(value = "cocontribution_pf_esi", encoded = true) String cocontribution_pf_esi,
            @Field(value = "rent_paid", encoded = true) String rent_paid,
            @Field(value = "electricity", encoded = true) String electricity,
            @Field(value = "books_periodicals", encoded = true) String books_periodicals,
            @Field(value = "advertisement", encoded = true) String advertisement,
            @Field(value = "recrument_place_conf", encoded = true) String recrument_place_conf,
            @Field(value = "insurance", encoded = true) String insurance,
            @Field(value = "legal", encoded = true) String legal,
            @Field(value = "subscription", encoded = true) String subscription,
            @Field(value = "charity_donation", encoded = true) String charity_donation,
            @Field(value = "auditor_consultancy", encoded = true) String auditor_consultancy,
            @Field(value = "rates_taxes", encoded = true) String rates_taxes,
            @Field(value = "bank_commission", encoded = true) String bank_commission,
            @Field(value = "emp_children_education", encoded = true) String emp_children_education,
            @Field(value = "status", encoded = true) String status);

    @FormUrlEncoded
    @POST("deleate_temadministrativeexp.php")
    Single<DeleteResponse> deleteadministrativeTemp(@Field(value = "branch_code", encoded = true) String branch_code);

    @FormUrlEncoded
    @POST("landlordoficegodown.php")
    Single<LandlordUpdateResponse> landlord(
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "landlord_id", encoded = true) String landlord_id,
            @Field(value = "cost_code", encoded = true) String cost_code,
            @Field(value = "location_address", encoded = true) String location_address,
            @Field(value = "landlord_name", encoded = true) String landlord_name,
            @Field(value = "landlord_address", encoded = true) String landlord_address,
            @Field(value = "pan_no", encoded = true) String pan_no,
            @Field(value = "off_gdn_residence", encoded = true) String off_gdn_residence,
            @Field(value = "period_from", encoded = true) String period_from,
            @Field(value = "period_to", encoded = true) String period_to,
            @Field(value = "rent_amount", encoded = true) String rent_amount,
            @Field(value = "ofgdn_area", encoded = true) String ofgdn_area,
            @Field(value = "open_area", encoded = true) String open_area);

    @FormUrlEncoded
    @POST("landlord_godownList.php")
    Single<LandlordGodownListResponse> landlorGodownList
            (@Field(value = "branch_code", encoded = true) String branch_code,
             @Field(value = "search", encoded = true) String search);

    @FormUrlEncoded
    @POST("addupdate_motercycle.php")
    Single<AddUpdateMoterCycleResponse> addMotercycle(
            @Field(value = "v_id", encoded = true) String v_id,
            @Field(value = "region_id", encoded = true) String region_id,
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "division_name", encoded = true) String division_name,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "branch_name", encoded = true) String branch_name,
            @Field(value = "vehicle_no", encoded = true) String vehicle_no,
            @Field(value = "regist_no", encoded = true) String regist_no,
            @Field(value = "reg_last_Date", encoded = true) String reg_last_Date,
            @Field(value = "make", encoded = true) String make,
            @Field(value = "model", encoded = true) String model,
            @Field(value = "vehicle_saleDate", encoded = true) String vehicle_saleDate,
            @Field(value = "ins_upto", encoded = true) String ins_upto,
            @Field(value = "puc_upto", encoded = true) String puc_upto,
            @Field(value = "standerd_km", encoded = true) String standerd_km,
            @Field(value = "insurance_no", encoded = true) String insurance_no,
            @Field(value = "user_person", encoded = true) String user_person);

    @FormUrlEncoded
    @POST("addupdate_weightmachine.php")
    Single<AddUpdatWeightMachineResponse> addWeightMachi(
            @Field(value = "w_id", encoded = true) String w_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "weightMc_No", encoded = true) String weightMc_No,
            @Field(value = "make", encoded = true) String make,
            @Field(value = "model", encoded = true) String model,
            @Field(value = "renew_upto", encoded = true) String renew_upto);


    @FormUrlEncoded
    @POST("motercycle_list.php")
    Single<MoterCycleListResponse> motercycleList(
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "search", encoded = true) String search,
            @Field(value = "status", encoded = true) String status);

    @FormUrlEncoded
    @POST("moterCycle_soldout.php")
    Single<SoldMoterCycleResponse> motercycleSold(
            @Field(value = "vehicle_no", encoded = true) String vehicle_no,
            @Field(value = "sold_status", encoded = true) String sold_status);


    @FormUrlEncoded
    @POST("weight_mc_list.php")
    Single<WeightMachineListResponse> weightMachineList(
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "search", encoded = true) String search);

    @FormUrlEncoded
    @POST("addConveyanceMobile.php")
    Single<AddConveyanceMobileResponse> addConveMobile(
            @Field(value = "ps_id", encoded = true) String ps_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "emp_code", encoded = true) String emp_code,
            @Field(value = "emp_name", encoded = true) String emp_name,
            @Field(value = "conveyance", encoded = true) String conveyance,
            @Field(value = "mobile", encoded = true) String mobile);


    @FormUrlEncoded
    @POST("addSweeperPeon.php")
    Single<AddSweeperPeonResponse> addSweeperPeon(
            @Field(value = "sp_id", encoded = true) String sp_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "emp_code", encoded = true) String emp_code,
            @Field(value = "emp_name", encoded = true) String emp_name,
            @Field(value = "emp_salary", encoded = true) String emp_salary,
            @Field(value = "sweeper_peon", encoded = true) String sweeper_peon);

    @FormUrlEncoded
    @POST("conveyance_mobileList.php")
    Single<ConveyanceMobileListResponse> conveyanceMobileList(
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "search", encoded = true) String search);

    @FormUrlEncoded
    @POST("sweeper_peonList.php")
    Single<SweeperPeonListResponse> sweeperPeonList(
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "sweeper_peon", encoded = true) String sweeper_peon);


    @FormUrlEncoded
    @POST("eployeeTransfer.php")
    Single<TransferEmployeeResponse> transferEmployee(
            @Field(value = "emp_code", encoded = true) String emp_code,
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "division_name", encoded = true) String division_name,
            @Field(value = "from_branch", encoded = true) String from_branch,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "branch_name", encoded = true) String branch_name);

    @FormUrlEncoded
    @POST("uaseActiveInactive.php")
    Single<CheckStatusResponse>
    statusCheck(@Field(value = "emp_code", encoded = true) String emp_code);

    @FormUrlEncoded
    @POST("update_DiviceToken.php")
    Single<SendDiviceTokenResponse>
    sentDeivceToken(@Field(value = "emp_code", encoded = true) String emp_code,
                    @Field(value = "token", encoded = true) String token);

    @FormUrlEncoded
    @POST("emp_TransferHilstoryList.php")
    Single<EmployeeTransferHistoryResponse>
    emp_transferHistoey(@Field(value = "emp_code", encoded = true) String emp_code);


    @FormUrlEncoded
    @POST("employeeRemarkList.php")
    Single<EmpRemarkListResponse>
    empRemarkList(@Field(value = "emp_code", encoded = true) String emp_code);

    @FormUrlEncoded
    @POST("deleteEmpRemark.php")
    Single<DeleteResponse>
    deleteEmpRemark(@Field(value = "r_id", encoded = true) String r_id);


    @FormUrlEncoded
    @POST("updateUserStatus.php")
    Single<EmployeeActive_InctiveResponse>
    actInactive(@Field(value = "emp_code", encoded = true) String emp_code,
                @Field(value = "status", encoded = true) String status);

    @FormUrlEncoded
    @POST("teeElectricity.php")
    Single<AddTeeElectricityResponse>
    addTeeElectricity(@Field(value = "branch_code", encoded = true) String branch_code,
                      @Field(value = "tea", encoded = true) String tea,
                      @Field(value = "electricity", encoded = true) String electricity);


    @FormUrlEncoded
    @POST("profileDetails.php")
    Single<ProfileResponseResponse> profile(@Field(value = "emp_code", encoded = true) String emp_code);

    @FormUrlEncoded
    @POST("addDailyBusiness.php")
    Single<AddUpdateDailyBookingResponse> addDailyBooking(
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "year", encoded = true) String year,
            @Field(value = "month", encoded = true) String month,
            @Field(value = "date", encoded = true) String date,
            @Field(value = "parcel", encoded = true) String parcel,
            @Field(value = "part", encoded = true) String part,
            @Field(value = "ftl", encoded = true) String ftl,
            @Field(value = "total", encoded = true) String total);

    @FormUrlEncoded
    @POST("addCustomerDetails.php")
    Single<AddCustomerDeatilsResponse> addcustomerDeatils(
            @Field(value = "cust_id", encoded = true) String cust_id,
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "m_name", encoded = true) String m_name,
            @Field(value = "company_name", encoded = true) String company_name,
            @Field(value = "cust_name", encoded = true) String cust_name,
            @Field(value = "cust_mobile", encoded = true) String cust_mobile,
            @Field(value = "cust_design", encoded = true) String cust_design,
            @Field(value = "cust_email", encoded = true) String cust_email,
            @Field(value = "noof_visit", encoded = true) String noof_visit,
            @Field(value = "cust_type", encoded = true) String cust_type);

    @FormUrlEncoded
    @POST("addCustFeedback.php")
    Single<FeedbackResponse> addCustFeedback(
            @Field(value = "cust_id", encoded = true) String cust_id,
            @Field(value = "fed_date", encoded = true) String fed_date,
            @Field(value = "feedback", encoded = true) String feedback);


    @FormUrlEncoded
    @POST("addVisitRemark.php")
    Single<AddRemarkResponse> addCustVisitRemark(
            @Field(value = "f_id", encoded = true) String f_id,
            @Field(value = "cust_id", encoded = true) String cust_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "remark", encoded = true) String remark);

    @FormUrlEncoded
    @POST("addEmployeeRemark.php")
    Single<AddRemarkResponse> addEmpRemark(
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "emp_code", encoded = true) String emp_code,
            @Field(value = "remark", encoded = true) String remark);

    @FormUrlEncoded
    @POST("customerDetailsList.php")
    Single<CustomerListResponse> customerList(
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "company_name", encoded = true) String company_name);

    @FormUrlEncoded
    @POST("customerFeedbackList.php")
    Single<FeedbackListResponse> feedbackList(@Field(value = "cust_id", encoded = true) String cust_id);

    @FormUrlEncoded
    @POST("custVisitremrkList.php")
    Single<RemarkListResponse> remarkList(@Field(value = "f_id", encoded = true) String f_id);

    @FormUrlEncoded
    @POST("vehiclePlaceFaild_List.php")
    Single<VehicleFailListResponse> failedList(@Field(value = "region_id", encoded = true) String region_id,
                                               @Field(value = "division_id", encoded = true) String division_id);


    @FormUrlEncoded
    @POST("addVehiclePlacwFaild.php")
    Single<AddFaildVehicleResponse> addFaildVehicle(
            @Field(value = "region_id", encoded = true) String region_id,
            @Field(value = "division_id", encoded = true) String division_id,
            @Field(value = "branch_code", encoded = true) String branch_code,
            @Field(value = "branch_name", encoded = true) String branch_name,
            @Field(value = "emp_code", encoded = true) String emp_code,
            @Field(value = "emp_name", encoded = true) String emp_name,
            @Field(value = "vehicle_type", encoded = true) String vehicle_type,
            @Field(value = "date", encoded = true) String date,
            @Field(value = "remark", encoded = true) String remark,
            @Field(value = "faild_by", encoded = true) String faild_by,
            @Field(value = "vehicle_cost", encoded = true) String vehicle_cost,
            @Field(value = "customer_rate", encoded = true) String customer_rate);

    @FormUrlEncoded
    @POST("addEmpMessage.php")
    Single<AddMessageResponse> senMessage(
            @Field(value = "emp_code", encoded = true) String emp_code,
            @Field(value = "emp_name", encoded = true) String emp_name,
            @Field(value = "emp_branch", encoded = true) String emp_branch,
            @Field(value = "message", encoded = true) String message);


    @FormUrlEncoded
    @POST("insidentalCompanyList.php")
    Single<InsCompanyResponse> insList(@Field(value = "region_id", encoded = true) String region_id,
                                       @Field(value = "search", encoded = true) String search);

    @FormUrlEncoded
    @POST("insidentalAmountList.php")
    Single<InsAmoutResponse> insAmtList(@Field(value = "company_code", encoded = true) String region_id);
}