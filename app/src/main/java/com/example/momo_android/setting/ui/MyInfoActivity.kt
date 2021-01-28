package com.example.momo_android.setting.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityMyInfoBinding
import com.example.momo_android.login.ui.MainLoginActivity
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.setting.ResponseWithdrawalData
import com.example.momo_android.util.SharedPreferenceController
import com.example.momo_android.util.showToast
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MyInfoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMyInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //뒤로가기
        initBackButton()

        //박스
        changePasswordClickListener()
        privacyPolicyClickListener()
        termsOfServiceClickListener()
        logoutClickListener()

        //회원탈퇴
        withdrawalClickListener()
    }

    //뒤로가기 버튼
    private fun initBackButton() {
        binding.imgBack.setOnClickListener {
            finish()
        }
    }
    //박스 1_ 비밀번호 변경
    private fun changePasswordClickListener(){
        binding.constraintlayoutBox1.setOnClickListener {}
    }
    //박스 2_ 개인정보처리방침
    private fun privacyPolicyClickListener(){
        binding.constraintlayoutBox2.setOnClickListener {}
    }
    //박스 3_서비스이용약관
    private fun termsOfServiceClickListener(){
        binding.constraintlayoutBox3.setOnClickListener {}
    }
    //박스 4_로그아웃
    private fun logoutClickListener(){
        binding.constraintlayoutBox4.setOnClickListener {
            val logoutDialog = LogoutDialogFragment.CustomDialogBuilder().create()
            logoutDialog.show(supportFragmentManager, logoutDialog.tag)
        }
    }

    //회원탈퇴
    private fun withdrawalClickListener(){
        binding.tvWithdrawal.setOnClickListener {
            //모달 보여주고 탈퇴처리
            val withdrawalModal = ModalWithdrawal(this)
            withdrawalModal.start()
            withdrawalModal.setOnClickListener {
                if(it == "확인") {
                    //회원탈퇴 서버연결+ 창닫기
                    deleteUser()

                    overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
                }
            }
        }
    }

    private fun deleteUser() {
        RequestToServer.service.getWithdrawal(
            Authorization = SharedPreferenceController.getAccessToken(this),
            params = SharedPreferenceController.getUserId(this)
        ).enqueue(object : retrofit2.Callback<ResponseWithdrawalData> {
            override fun onResponse(
                call: Call<ResponseWithdrawalData>,
                response: Response<ResponseWithdrawalData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        Log.d(
                            "Withdrawal_delete",
                            "success : ${response.body()!!.data}, message : ${response.message()}"
                        )

                        val withdrawal_intent=Intent(this@MyInfoActivity,MainLoginActivity::class.java)
                        startActivity(withdrawal_intent)
                        finishAffinity() // 전체 Activity 종료

                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseWithdrawalData>, t: Throwable) {
                Log.d("Withdrawal_fail", "fail : ${t.message}")
            }

        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        this.showToast(ob.getString("message"))
        Log.d("Withdrawal", ob.getString("message"))
    }



}
