package com.arcltd.staff.networkhandler.presentor;

import android.content.Context;
import android.util.Log;


import com.arcltd.staff.R;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorStatus;
import com.arcltd.staff.networkhandler.networkCallBack.INetworkResult;
import com.arcltd.staff.networkhandler.networkCallBack.IResultView;
import com.arcltd.staff.networkhandler.networkCallBack.NetworkHandler;
import com.arcltd.staff.networkhandler.remote.WebService;
import com.arcltd.staff.utility.ELog;

import java.net.UnknownHostException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class ApiPresenter implements INetworkResult {
    private static final String TAG = ApiPresenter.class.getSimpleName();

    private final WebService webService;
    private NetworkHandler networkHandler;
    private IResultView iResultViewListener;
    private Context mContext;

    public ApiPresenter(BaseActivity context, WebService webService, IResultView iResultView) {
        this.iResultViewListener = iResultView;
        this.webService = webService;
        networkHandler = new NetworkHandler(this);
        mContext = context;
    }


    @Override
    public void onSuccess(String responseBody, int responseType) {
        iResultViewListener.showResult(responseBody, responseType);
    }

    @Override
    public void onError(String message, int responseType, int errorCode) {
        iResultViewListener.onDisplayMessage(message, responseType, errorCode);
    }

    private void returnErrorMessage(Throwable throwable, int reqCode) {
        try {
            if (throwable instanceof HttpException) {
                HttpException exception = (HttpException) throwable;
                iResultViewListener.onDisplayMessage(throwable.getMessage(), reqCode, exception.code());
            } else if (throwable instanceof OnErrorNotImplementedException) {
                OnErrorNotImplementedException exception = (OnErrorNotImplementedException) throwable;
                iResultViewListener.onDisplayMessage(throwable.getMessage(), reqCode, ErrorStatus.PAGE_NOT_FOUND);
            } else if (throwable instanceof IllegalStateException) {
                IllegalStateException exception = (IllegalStateException) throwable;
                iResultViewListener.onDisplayMessage(throwable.getMessage(), reqCode, ErrorStatus.INTERNAL_SERVER_ERROR);
            } else if (throwable instanceof UnknownHostException) {
                UnknownHostException exception = (UnknownHostException) throwable;
                iResultViewListener.onDisplayMessage(throwable.getMessage(), reqCode, ErrorStatus.INTERNAL_SERVER_ERROR);
            } else if (throwable instanceof Exception) {
                Exception exception = (Exception) throwable;
                iResultViewListener.onDisplayMessage(throwable.getMessage(), reqCode, ErrorStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in returnErrorMessage", e);
            if (throwable.getMessage() != null) {
                iResultViewListener.onDisplayMessage(throwable.getMessage(), reqCode, ErrorStatus.INTERNAL_SERVER_ERROR);
            } else {
                iResultViewListener.onDisplayMessage(mContext.getString(R.string.server_error_message), reqCode, ErrorStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * user registration data @emits from the server
     */

    public void userregistration(CompositeDisposable disposable, int requestCode, String name, String email,
                                 String password, String contactno, String login_type,
                                 String region_id, String region_name, String division_id,
                                 String division_name, String branch_code,String branch_name, String emp_id, String design,
                                 String status, String mess_name, String vari_status) {
        Log.e(TAG, "userRegister: " + " Name-" + name + " Email-" + email + " Password-" + password +
                " Contact_No-" + contactno + " region_id-" + region_id + " region_name-" + region_name + " division_id-" + division_id
                + " division_name-" + division_name + " branch_code-" + branch_code+ " branch_name-" + branch_name + " emp_id-"
                + emp_id + " design-" + design + " vari_status-" + vari_status);
        disposable.add(
                webService.signup(name, email, password, contactno, login_type, region_id, region_name, division_id
                                , division_name, branch_code,branch_name, emp_id, design, status, mess_name, vari_status)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void getCrashReport(CompositeDisposable disposable, final int requestCode, String request_data,
                               String type) {
        Log.e(TAG, "getCrashReport: " + "  https://niharexpress.com/appApi/api?" +
                 "&request_data=" + request_data + "&type=" + type );

        disposable.add(
                webService.getCrashData(request_data, type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void getCrashList(CompositeDisposable disposable, int requestCode, String region_id,String search) {
        Log.e(TAG, "getUserList   " + " region_id-" + region_id+ " search-" + search);
        disposable.add(
                webService.list_crash(region_id,search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }




    public void getUserPermissionList(CompositeDisposable disposable, int requestCode, String region_id,String search) {
        Log.e(TAG, "getUserList   " + " region_id-" + region_id+ " search-" + search);
        disposable.add(
                webService.list_userPermission(region_id,search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }



    public void userAppPermission(CompositeDisposable disposable, int requestCode,
                                 String region_id,  String emp_code, String div_branches,
                                 String div_mess, String send_profile, String emp_list,
                                 String emp_birthday, String retire_list, String motercy_list,
                                 String inciden_list, String weightmc_list, String conveyance,
                                 String lanlord_details, String admin_exp_list, String sendEmp_message,
                                 String add_mess, String add_emp, String add_convence_mobExp,
                                 String add_sweeperPeon, String transfer_emp, String active_deactiveEmp) {
        Log.e(TAG, "userRegister: " + " region_id-" + region_id + " emp_code-" + emp_code
                + " div_branches-" + div_branches + " div_mess-" + div_mess + " send_profile-" + send_profile
                + " emp_list-" + emp_list + " emp_birthday-" + emp_birthday
                + " retire_list-" + retire_list + " motercy_list-" + motercy_list + " inciden_list-"+inciden_list
                + " weightmc_list-" + weightmc_list + " conveyance-" + conveyance + " lanlord_details-"+lanlord_details
                + " admin_exp_list-" + admin_exp_list + " sendEmp_message-" + sendEmp_message + " add_mess-"+add_mess
                +"add_emp"+ add_emp + " add_convence_mobExp-" + add_convence_mobExp
                +"add_sweeperPeon"+ add_sweeperPeon + " transfer_emp-" + transfer_emp + " active_deactiveEmp-" + active_deactiveEmp);
        disposable.add(
                webService.appPermission(region_id, emp_code, div_branches
                                , div_mess, send_profile, emp_list, emp_birthday, retire_list, motercy_list,
                                inciden_list,weightmc_list,conveyance,lanlord_details,admin_exp_list,
                                sendEmp_message,add_mess,add_emp,add_convence_mobExp,add_sweeperPeon,
                                transfer_emp,active_deactiveEmp)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void getUpdatePic(CompositeDisposable disposable, int requestCode, String email,String pic) {
        Log.e(TAG, " " + " email-" + email);
        disposable.add(
                webService.getUpPic(email,pic)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void getSignupOtp(CompositeDisposable disposable, int requestCode, String email) {
        Log.e(TAG, " " + " email-" + email);
        disposable.add(
                webService.getOTP(email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void getForgotPass(CompositeDisposable disposable, int requestCode, String email, String password, String cpassword) {
        Log.e(TAG, " " + " email-" + email + " password-" + password + " cpassword-" + cpassword);
        disposable.add(
                webService.getForgot(email, password, cpassword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void getEmpDetails(CompositeDisposable disposable, int requestCode, String emp_code) {
        Log.e(TAG, " " + " emp_code-" + emp_code);
        disposable.add(
                webService.list_empSignoup(emp_code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void submitSignOTP(CompositeDisposable disposable, int requestCode, String email,
                              String code, String status, String vari_status) {
        Log.e(TAG, " " + " email-" + email + " status-" + status + " code-" + code + " vari_status-" + vari_status);
        disposable.add(
                webService.SubmitOtp(email, code, status, vari_status)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void addEmployee(CompositeDisposable disposable, int requestCode,
                            String region_name, String region_id, String division_name, String division_id,
                            String branch_code, String branch_name, String emp_act_retire,
                            String emp_code, String emp_name, String emp_eqalification, String emp_joining_date,
                            String emp_desig, String emp_salary, String status, String dat_of_birth, String uan_no) {
        Log.e(TAG, "Add Employee/Update : " + " region_name-" + region_name + " region_id-" + region_id +
                " division_name-" + division_name + " division_id-" + division_id + " branch_code-" + branch_code +
                " branch_name-" + branch_name + " emp_act_retire-" + emp_act_retire + " emp_code-" + emp_code +
                " emp_name-" + emp_name + " emp_eqalification-" + emp_eqalification + " emp_joining_date-" + emp_joining_date +
                " emp_desig-" + emp_desig + " emp_salary-" + emp_salary + " status-" + status + " dat_of_birth-" + dat_of_birth
                + " uan_no-" + uan_no);
        disposable.add(
                webService.addEmployee(region_name, region_id, division_name, division_id
                                , branch_code, branch_name, emp_act_retire, emp_code, emp_name, emp_eqalification,
                                emp_joining_date, emp_desig, emp_salary, status, dat_of_birth, uan_no)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void userLogin(CompositeDisposable disposable, int requestCode, String email, String password) {
        Log.e(TAG, " " + " Email-" + email + " Passwprd-" + password);
        disposable.add(
                webService.login(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void userResetPass(CompositeDisposable disposable, int requestCode, String email, String password,
                              String newPassword) {
        Log.e(TAG, " " + " Email-" + email + " Passwprd-" + password + " New Passwprd-" + newPassword);
        disposable.add(
                webService.resetPass(email, password, newPassword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void list_region(CompositeDisposable disposable, int requestCode) {
        //  Log.e(TAG, " " + " Email-" + email + " Passwprd-" + password);
        disposable.add(
                webService.list_region()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void list_division(CompositeDisposable disposable, int requestCode, String division_id) {
        Log.e(TAG, " " + " division_id-" + division_id);
        disposable.add(
                webService.list_division(division_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void list_Incharge(CompositeDisposable disposable, int requestCode, String division_id) {
        Log.e(TAG, " " + " division_id-" + division_id);
        disposable.add(
                webService.list_Incharges(division_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void messageList(CompositeDisposable disposable, int requestCode, String emp_code) {
        Log.e(TAG, " " + " emp_code-" + emp_code);
        disposable.add(
                webService.list_message(emp_code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void deleteAllEmployee(CompositeDisposable disposable, int requestCode, String resionId) {
        Log.e(TAG, " " + " resionId-" + resionId);
        disposable.add(
                webService.deleteEmployee(resionId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void messEMPList(CompositeDisposable disposable, int requestCode, String mes_id) {
        Log.e(TAG, " " + " mes_id-" + mes_id);
        disposable.add(
                webService.messEmployeeList(mes_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void target(CompositeDisposable disposable, int requestCode, String branch_code, String search) {
        Log.e(TAG, " " + " branch_code-" + branch_code + "   search" + search);
        disposable.add(
                webService.target(branch_code, search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void list_employee(CompositeDisposable disposable, int requestCode, String branch_code,
                              String search) {
        Log.e(TAG, " " + " branch_code-" + branch_code + " search-" + search);
        disposable.add(
                webService.list_emp(branch_code, search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void listBirth_employee(CompositeDisposable disposable, int requestCode, String region_id,
                                   String search) {
        Log.e(TAG, " " + " region_id-" + region_id + " search-" + search);
        disposable.add(
                webService.listBirth_emp(region_id, search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void retireList_employee(CompositeDisposable disposable, int requestCode, String branch_code,
                                    String search) {
        Log.e(TAG, " " + "Retirement branch_code-" + branch_code + " search-" + search);
        disposable.add(
                webService.retireList_emp(branch_code, search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void list_ofiicemessgdn(CompositeDisposable disposable, int requestCode, String div_id, String search) {
        Log.e(TAG, " " + " div_id-" + div_id + "search" + search);
        disposable.add(
                webService.list_ofiicemessgdn(div_id, search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void list_mess(CompositeDisposable disposable, int requestCode, String messName) {
        Log.e(TAG, " " + " messName-" + messName);
        disposable.add(
                webService.list_mess(messName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void list_branch(CompositeDisposable disposable, int requestCode, String branch_code, String division_id) {
        Log.e(TAG, " " + " branch_code-" + branch_code + "division_id" + division_id);
        disposable.add(
                webService.list_branch(branch_code, division_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void list_branchPincode(CompositeDisposable disposable, int requestCode, String branch_code, String division_id) {
        Log.e(TAG, " " + " branch_code-" + branch_code + "division_id" + division_id);
        disposable.add(
                webService.list_branchPincodeWise(branch_code, division_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void addMess(CompositeDisposable disposable, int requestCode, String region_id,
                        String messName, String messAddress, String cookSalarhy, String division_Name, String ownRented
            , String cookAvailable) {
        Log.e(TAG, "AddMess: " + " region_id-" + region_id + " messName-" + messName
                + " messAddress-" + messAddress +
                " cookSalarhy-" + cookSalarhy +
                " division_Name-" + division_Name +
                " ownRented-" + ownRented +
                " cookAvailable-" + cookAvailable);
        disposable.add(
                webService.addMESS(region_id, messName, messAddress, cookSalarhy, division_Name
                                , ownRented, cookAvailable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void advExp(CompositeDisposable disposable, int requestCode, String branch_code) {
        Log.e(TAG, " " + " branch_code-" + branch_code);
        disposable.add(
                webService.administrati_exp(branch_code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void administrationExp(CompositeDisposable disposable, int requestCode, String region_id, String division_id,
                                  String branch_code, String telephone, String pastge_courier,
                                  String print_stationery, String bike_petrol_exp,
                                  String bike_maitainance, String car_petrol_exp,
                                  String car_maitainance, String conveyance,
                                  String travelling, String staff_welfare,
                                  String misc_charges, String busi_pramostion,
                                  String office_maitanance, String office_eqp_maintenance,
                                  String repare_buildings, String salary_bonas_gradu_other,
                                  String cocontribution_pf_esi, String rent_paid,
                                  String electricity, String books_periodicals,
                                  String advertisement, String recrument_place_conf,
                                  String insurance, String legal,
                                  String subscription, String charity_donation,
                                  String auditor_consultancy, String rates_taxes,
                                  String bank_commission, String emp_children_education,
                                  String status) {
        Log.e(TAG, "userRegister: " + " region_id-" + region_id + " division_id-" + division_id
                + " branch_code-" + branch_code + " telephone-" + telephone + " pastge_courier-" + pastge_courier +
                " print_stationery-" + print_stationery + " bike_petrol_exp-" + bike_petrol_exp + " bike_maitainance-" + bike_maitainance +
                " conveyance-" + conveyance + " travelling-" + travelling + " staff_welfare-" + staff_welfare
                + " misc_charges-" + misc_charges + " busi_pramostion-" + busi_pramostion + " office_maitanance-" + office_maitanance
                + " repare_buildings-" + repare_buildings + " salary_bonas_gradu_other-" + salary_bonas_gradu_other + " cocontribution_pf_esi-" + cocontribution_pf_esi
                + " rent_paid-" + rent_paid + " electricity-" + electricity + " books_periodicals-" + books_periodicals
                + " advertisement-" + advertisement + " recrument_place_conf-" + recrument_place_conf + " insurance-" + insurance
                + " legal-" + legal
                + " subscription-" + subscription + " charity_donation-" + charity_donation
                + " auditor_consultancy-" + auditor_consultancy + " rates_taxes-" + rates_taxes
                + " bank_commission-" + bank_commission + " emp_children_education-" + emp_children_education
                + " status-" + status);
        disposable.add(
                webService.addupdateAdminExp(region_id, division_id, branch_code, telephone, pastge_courier, print_stationery,
                                bike_petrol_exp, bike_maitainance, car_petrol_exp, car_maitainance, conveyance, travelling,
                                staff_welfare, misc_charges, busi_pramostion, office_maitanance, office_eqp_maintenance,
                                repare_buildings, salary_bonas_gradu_other, cocontribution_pf_esi, rent_paid,
                                electricity, books_periodicals, advertisement, recrument_place_conf, insurance,
                                legal, subscription, charity_donation, auditor_consultancy, rates_taxes, bank_commission,
                                emp_children_education, status)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void list_administrative(CompositeDisposable disposable, int requestCode, String region_id) {
        Log.e(TAG, " " + " region_id-" + region_id);
        disposable.add(
                webService.list_administrative(region_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void finaladministrationExp(CompositeDisposable disposable, int requestCode, String region_id, String division_id,
                                       String branch_code, String telephone, String pastge_courier,
                                       String print_stationery, String bike_petrol_exp,
                                       String bike_maitainance, String car_petrol_exp,
                                       String car_maitainance, String conveyance,
                                       String travelling, String staff_welfare,
                                       String misc_charges, String busi_pramostion,
                                       String office_maitanance, String office_eqp_maintenance,
                                       String repare_buildings, String salary_bonas_gradu_other,
                                       String cocontribution_pf_esi, String rent_paid,
                                       String electricity, String books_periodicals,
                                       String advertisement, String recrument_place_conf,
                                       String insurance, String legal,
                                       String subscription, String charity_donation,
                                       String auditor_consultancy, String rates_taxes,
                                       String bank_commission, String emp_children_education,
                                       String status) {
        Log.e(TAG, "userRegister: " + " region_id-" + region_id + " division_id-" + division_id
                + " branch_code-" + branch_code + " telephone-" + telephone + " pastge_courier-" + pastge_courier +
                " print_stationery-" + print_stationery + " bike_petrol_exp-" + bike_petrol_exp + " bike_maitainance-" + bike_maitainance +
                " conveyance-" + conveyance + " travelling-" + travelling + " staff_welfare-" + staff_welfare
                + " misc_charges-" + misc_charges + " busi_pramostion-" + busi_pramostion + " office_maitanance-" + office_maitanance
                + " repare_buildings-" + repare_buildings + " salary_bonas_gradu_other-" + salary_bonas_gradu_other + " cocontribution_pf_esi-" + cocontribution_pf_esi
                + " rent_paid-" + rent_paid + " electricity-" + electricity + " books_periodicals-" + books_periodicals
                + " advertisement-" + advertisement + " recrument_place_conf-" + recrument_place_conf + " insurance-" + insurance
                + " legal-" + legal
                + " subscription-" + subscription + " charity_donation-" + charity_donation
                + " auditor_consultancy-" + auditor_consultancy + " rates_taxes-" + rates_taxes
                + " bank_commission-" + bank_commission + " emp_children_education-" + emp_children_education
                + " status-" + status);
        disposable.add(
                webService.finaladdupdateAdminExp(region_id, division_id, branch_code, telephone, pastge_courier, print_stationery,
                                bike_petrol_exp, bike_maitainance, car_petrol_exp, car_maitainance, conveyance, travelling,
                                staff_welfare, misc_charges, busi_pramostion, office_maitanance, office_eqp_maintenance,
                                repare_buildings, salary_bonas_gradu_other, cocontribution_pf_esi, rent_paid,
                                electricity, books_periodicals, advertisement, recrument_place_conf, insurance,
                                legal, subscription, charity_donation, auditor_consultancy, rates_taxes, bank_commission,
                                emp_children_education, status)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void delete(CompositeDisposable disposable, int requestCode, String branch_code) {
        Log.e(TAG, " " + " branch_code-" + branch_code);
        disposable.add(
                webService.deleteadministrativeTemp(branch_code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void landlordUpdate(CompositeDisposable disposable, int requestCode, String branch_code,
                               String landlord_id, String cost_code, String location_address,
                               String landlord_name, String landlord_address, String pan_no,
                               String off_gdn_residence, String period_from, String period_to,
                               String rent_amount, String ofgdn_area, String open_area) {
        Log.e(TAG, "userRegister: " + " branch_code-" + branch_code + " landlord_id-" + landlord_id +
                " cost_code-" + cost_code + "location_address-" + location_address + " landlord_name-" + landlord_name +
                " landlord_address-" + landlord_address + " pan_no-" + pan_no +
                " off_gdn_residence-" + off_gdn_residence + " period_from-" + period_from + "" +
                " period_to-" + period_to + " rent_amount-" + rent_amount + ""
                + " ofgdn_area-" + ofgdn_area + " open_area-" + open_area);
        disposable.add(
                webService.landlord(branch_code, landlord_id, cost_code, location_address, landlord_name,
                                landlord_address, pan_no, off_gdn_residence, period_from, period_to, rent_amount, ofgdn_area,
                                open_area)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void landLordGdnList(CompositeDisposable disposable, int requestCode, String branch_code, String search) {
        Log.e(TAG, " " + " branch_code-" + branch_code);
        disposable.add(
                webService.landlorGodownList(branch_code, search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void addMoterCycle(CompositeDisposable disposable, int requestCode, String v_id,
                              String region_id,
                              String division_id,
                              String division_name,
                              String branch_code,
                              String branch_name,
                              String vehicle_no,
                              String regist_no,
                              String reg_last_Date,
                              String make,
                              String model,
                              String vehicle_saleDate,
                              String ins_upto, String puc_upto,
                              String standerd_km,String insurance_no,String user_person) {
        Log.e(TAG, "userRegister: " + " v_id-" + v_id + " branch_code-" + branch_code +
                " vehicle_no-" + vehicle_no +
                " regist_no-" + regist_no + "make-" + make + " model-" + model +
                " ins_upto-" + ins_upto + " puc_upto-" + puc_upto);
        disposable.add(
                webService.addMotercycle(v_id,region_id,division_id,division_name, branch_code,branch_name,
                                vehicle_no, regist_no,reg_last_Date, make, model,vehicle_saleDate,
                                ins_upto, puc_upto,standerd_km,insurance_no,user_person)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void addWeightMC(CompositeDisposable disposable, int requestCode, String w_id, String branch_code,
                            String weightMc_No, String make,
                            String model, String renew_upto) {
        Log.e(TAG, "userRegister: " + " w_id-" + w_id + " branch_code-" + branch_code +
                " weightMc_No-" + weightMc_No
                + "make-" + make + " model-" + model + " renew_upto-" + renew_upto);
        disposable.add(
                webService.addWeightMachi(w_id, branch_code, weightMc_No, make, model, renew_upto)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void motercycleList(CompositeDisposable disposable, int requestCode, String branch_code,
                               String search, String status) {
        Log.e(TAG, " " + " branch_code-" + branch_code + " status " + status);
        disposable.add(
                webService.motercycleList(branch_code, search, status)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void motercyclesld(CompositeDisposable disposable, int requestCode, String vehicle_no, String sold_status) {
        Log.e(TAG, " " + " vehicle_no-" + vehicle_no + "  sold_status" + sold_status);
        disposable.add(
                webService.motercycleSold(vehicle_no, sold_status)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void weightMc(CompositeDisposable disposable, int requestCode, String branch_code, String search) {
        Log.e(TAG, " " + " branch_code-" + branch_code);
        disposable.add(
                webService.weightMachineList(branch_code, search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void addConveyanceMobile(CompositeDisposable disposable, int requestCode, String ps_id,
                                    String branch_code, String emp_code, String emp_name,
                                    String conveyance, String mobile) {
        Log.e(TAG, "userRegister: " + " ps_id-" + ps_id + " branch_code-" + branch_code +
                " emp_code-" + emp_code
                + "emp_name-" + emp_name + " conveyance-" + conveyance + " renew_upto-" + mobile);
        disposable.add(
                webService.addConveMobile(ps_id, branch_code, emp_code, emp_name, conveyance, mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void addSweeperPeon(CompositeDisposable disposable, int requestCode, String sp_id,
                               String branch_code, String emp_code, String emp_name,
                               String emp_salary, String sweeper_peon) {
        Log.e(TAG, "userRegister: " + " sp_id-" + sp_id + " branch_code-" + branch_code +
                " emp_code-" + emp_code
                + "emp_name-" + emp_name + " emp_salary-" + emp_salary + " sweeper_peon-" + sweeper_peon);
        disposable.add(
                webService.addSweeperPeon(sp_id, branch_code, emp_code, emp_name, emp_salary, sweeper_peon)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void convenceList(CompositeDisposable disposable, int requestCode, String branch_code, String search) {
        Log.e(TAG, " " + " branch_code-" + branch_code);
        disposable.add(
                webService.conveyanceMobileList(branch_code, search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void sweeperPeonList(CompositeDisposable disposable, int requestCode, String branch_code,
                                String sweeper_peon) {
        Log.e(TAG, " " + " branch_code-" + branch_code + " sweeper_peon-" + sweeper_peon);
        disposable.add(
                webService.sweeperPeonList(branch_code, sweeper_peon)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void empTransfer(CompositeDisposable disposable, int requestCode, String empCode, String division_id,
                            String division_Name, String from_branch, String branch_code, String branch_Name) {
        Log.e(TAG, "Transfer Employee : " + " empCode-" + empCode + " division_id-" + division_id + "" +
                " division_Name-" + division_Name +
                " from_branch-" + from_branch +
                " branch_code-" + branch_code + " branch_code-" + branch_code);
        disposable.add(
                webService.transferEmployee(empCode, division_id, division_Name, from_branch, branch_code, branch_Name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void checkStaus(CompositeDisposable disposable, int requestCode, String emp_code) {
        Log.e(TAG, " " + " emp_code-" + emp_code);
        disposable.add(
                webService.statusCheck(emp_code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void sendToken(CompositeDisposable disposable, int requestCode, String emp_code, String token) {
        Log.e(TAG, " " + " emp_code-" + emp_code + " token-" + token);
        disposable.add(
                webService.sentDeivceToken(emp_code, token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void empTransHistory(CompositeDisposable disposable, int requestCode, String emp_code) {
        Log.e(TAG, " " + " emp_code-" + emp_code);
        disposable.add(
                webService.emp_transferHistoey(emp_code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void empRemarkList(CompositeDisposable disposable, int requestCode, String emp_code) {
        Log.e(TAG, " " + " emp_code-" + emp_code);
        disposable.add(
                webService.empRemarkList(emp_code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void deleteEmpRemark(CompositeDisposable disposable, int requestCode, String r_id) {
        Log.e(TAG, " " + " emp_code-" + r_id);
        disposable.add(
                webService.deleteEmpRemark(r_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void empActiveInacytive(CompositeDisposable disposable, int requestCode, String emp_id, String status) {
        Log.e(TAG, " " + " emp_id-" + emp_id + " status-" + status);
        disposable.add(
                webService.actInactive(emp_id, status)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void addTeeElectricity(CompositeDisposable disposable, int requestCode, String branch_code,
                                  String tee, String electricity) {
        Log.e(TAG, " " + " branch_code-" + branch_code + " tee-" + tee + " electricity-" + electricity);
        disposable.add(
                webService.addTeeElectricity(branch_code, tee, electricity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


    public void profile(CompositeDisposable disposable, int requestCode, String branch_code) {
        Log.e(TAG, " " + " branch_code-" + branch_code);
        disposable.add(
                webService.profile(branch_code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void addDailyBookig(CompositeDisposable disposable, int requestCode, String branch_code, String year,
                               String month, String date, String parcel, String part, String ftl, String total) {
        Log.e(TAG, "Daily Booking : " + " branch_code-" + branch_code + " year-" + year + "" +
                " month-" + month + " date-" + date + " parcel-" + parcel + " part-" + part +
                " ftl-" + ftl + " total-" + total);
        disposable.add(
                webService.addDailyBooking(branch_code, year, month, date, parcel, part, ftl, total)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void addCustomer(CompositeDisposable disposable, int requestCode, String cust_id, String division_id,
                            String branch_code, String m_name, String company_name, String cust_name,
                            String cust_mobile, String cust_design, String cust_email, String noof_visit, String cust_type) {
        Log.e(TAG, "addCustomer : " + " cust_id-" + cust_id + " division_id-" + division_id + "" +
                " branch_code-" + branch_code + " m_name-" + m_name + " company_name-" + company_name + " cust_name-" + cust_name +
                " cust_mobile-" + cust_mobile + " cust_design-" + cust_design +
                " cust_email-" + cust_email + " noof_visit-" + noof_visit + " total-" + cust_type);
        disposable.add(
                webService.addcustomerDeatils(cust_id, division_id, branch_code, m_name, company_name, cust_name,
                                cust_mobile, cust_design, cust_email, noof_visit, cust_type)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void addCustFeedback(CompositeDisposable disposable, int requestCode, String cust_id, String fed_date,
                                String feedback) {
        Log.e(TAG, "addCustomer : " + " cust_id-" + cust_id + " fed_date-" + fed_date + "" +
                " feedback-" + feedback);
        disposable.add(
                webService.addCustFeedback(cust_id, fed_date, feedback)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void addCustRemark(CompositeDisposable disposable, int requestCode, String f_id, String cust_id,
                              String branch_code, String remark) {
        Log.e(TAG, "addCustomer : " + " f_id-" + f_id + " cust_id-" + cust_id + "" + " branch_code-" + branch_code + "" +
                " remark-" + remark);
        disposable.add(
                webService.addCustVisitRemark(f_id, cust_id, branch_code, remark)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void addEmpRemark(CompositeDisposable disposable, int requestCode, String branch_code,
                             String emp_code, String remark) {
        Log.e(TAG, "addCustomer : " + " branch_code-" + branch_code + "" + " emp_code-" + emp_code + "" +
                " remark-" + remark);
        disposable.add(
                webService.addEmpRemark(branch_code, emp_code, remark)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void list_Customer(CompositeDisposable disposable, int requestCode, String division_id,
                              String company_name) {
        Log.e(TAG, " " + " division_id-" + division_id);
        disposable.add(
                webService.customerList(division_id, company_name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void feedbackList(CompositeDisposable disposable, int requestCode, String cust_id) {
        Log.e(TAG, " " + " cust_id-" + cust_id);
        disposable.add(
                webService.feedbackList(cust_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void remarkList(CompositeDisposable disposable, int requestCode, String f_id) {
        Log.e(TAG, " " + " f_id-" + f_id);
        disposable.add(
                webService.remarkList(f_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void vaehicleFailedList(CompositeDisposable disposable, int requestCode,
                                   String region_id, String division_id) {
        Log.e(TAG, " " + " region_id-" + region_id + " division_id-" + division_id);
        disposable.add(
                webService.failedList(region_id, division_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void addFaild(CompositeDisposable disposable, int requestCode, String region_id, String division_id,
                         String branch_code, String branch_name, String emp_code, String emp_name,
                         String vehicle_type, String date, String remark, String faild_by, String vehicleCost, String customerRate) {
        Log.e(TAG, "addFaild_Vehicle : " + " region_id-" + region_id + " division_id-" + division_id + ""
                + " branch_code-" + branch_code + " branch_name-" + branch_name + " emp_code-" + emp_code
                + " emp_name-" + emp_name + " vehicle_type-" + vehicle_type + " date-" + date
                + " remark-" + remark + " faild_by-" + faild_by + " vehicleCost-" + vehicleCost + " customerRate-" + customerRate);
        disposable.add(
                webService.addFaildVehicle(region_id, division_id, branch_code, branch_name,
                                emp_code, emp_name, vehicle_type, date, remark, faild_by, vehicleCost, customerRate)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void addMessage(CompositeDisposable disposable, int requestCode, String emp_code, String emp_name,
                           String empBranch, String message) {
        Log.e(TAG, "addFaild_Vehicle : " + " emp_code-" + emp_code + " emp_name-" + emp_name + ""
                + " empBranch-" + empBranch + " message-");
        disposable.add(
                webService.senMessage(emp_code, emp_name, empBranch, message)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void insCompanyList(CompositeDisposable disposable, int requestCode,
                               String region_id, String search) {
        Log.e(TAG, " " + " region_id-" + region_id + " search-" + search);
        disposable.add(
                webService.insList(region_id, search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }

    public void insAmtAllList(CompositeDisposable disposable, int requestCode,
                              String company_code) {
        Log.e(TAG, " " + " company_code-" + company_code);
        disposable.add(
                webService.insAmtList(company_code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Object, Object>() {
                            @Override
                            public Object apply(Object blog) throws Exception {
                                return blog;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<Object>() {
                            @Override
                            public void onSuccess(Object object) {
                                iResultViewListener.showResult(object, requestCode);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                returnErrorMessage(throwable, requestCode);
                            }
                        })
        );
    }


}
