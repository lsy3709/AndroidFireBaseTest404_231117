package com.sylovestp.firebasetest.imageShareApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope


import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.recaptcha.Recaptcha
import com.google.android.recaptcha.RecaptchaAction
import com.google.android.recaptcha.RecaptchaClient
import com.google.firebase.auth.GoogleAuthProvider
import com.sylovestp.firebasetest.imageShareApp.MyApplication.Companion.db
import com.sylovestp.firebasetest.imageShareApp.MyApplication.Companion.storage
import com.sylovestp.firebasetest.imageShareApp.databinding.ActivityAuthBinding
import com.sylovestp.firebasetest.imageShareApp.imageShareApp.MainImageShareAppActivity



import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    lateinit var binding : ActivityAuthBinding
    //리캡챠
    private lateinit var recaptchaClient: RecaptchaClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // 리캡차 초기화
        initializeRecaptchaClient()



        // 다음 시간에, 이메일, 구글 인증, 후
        // 인증 후 2번째 페이지 접근 확인하고,
        // 그리고 나서, 2번째 페이지 부분을 아래의 기능으로 구성.
        // 스토어, 스토리지 에 접근 및 이용하는 부분 보여 주기.

        //MyApplication 인증 함수를 이용해서,
    // 인증이 되면, 특정 뷰로 인증됨을 확인
        if(MyApplication.checkAuth()){
            Log.d("lsy","로그인 인증이 됨")
            // 인증 되면, mode에 따라 보여주는 함수를 동작 시키기
            chageVisi("login")
        } else {
            Log.d("lsy","로그인 인증이 안됨")
            // 인증 되면, mode에 따라 보여주는 함수를 동작 시키기
            chageVisi("logout")
        }
        // 구글 로그인 기능 확인.
        // 1) 구글 인증 버튼 눌러서 , 2) 후처리 함수를 호출 하기.
        // 1) 구글 인증 버튼, 구글의 파이어베이스 서버에 접속하고, 관련 인증을 가지고 돌아오기.
        // 2) 후처리 함수 만들기. 구글의 계정의 정보를 가지고와서, 처리하는 로직.
        val requestLauncher = registerForActivityResult(
            // 후처리 하는 함수가 정해져 있는데, 이함수를 인증 , 권한 확인용
            ActivityResultContracts.StartActivityForResult()
        ){
            // 실제 작업은 여기 이루어짐.
            // 구글 인증 결과 처리.
            // it.data 이부분이 구글로부터 받아온 계정 정보.
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            // 로그인 정보가 있는지? 없는지? 또는 네트워크 연결 오류등으로
            // 정보가 받거나 못 받거나 할 가능성이 있으면, 무조건, try catch 구문 사용함.
            try {
                // 계정 정보 가져오기.
                val account = task.getResult(ApiException::class.java)
                // 계정의 정보 가져오기.
                val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                // 우리가 만든 MyApplication에서 auth로 확인
                MyApplication.auth.signInWithCredential(credential)
                    // 정보를 잘 가지고 왔을 때 , 수행이 될 콜백함수,
                    .addOnCompleteListener(this) {
                        // 수행할 작업.
                        task ->
                        if(task.isSuccessful){
                            MyApplication.email = account.email
                            chageVisi("login")

                        } else {
                            chageVisi("logout")
                        }
                    }
            } catch (e:ApiException){
                chageVisi("logout")
            }
        }

        // 구글 인증 버튼 클릭시, 해당 구글 계정 선택 화면으로 이동하는 인텐트 추가하기.
        binding.googleAuthInBtn.setOnClickListener {
        // 샘플 코드
            // 옵션, 이메일, 아이디토큰 가져오는 옵션
            val gso = GoogleSignInOptions
                // 오타, DEFAULT_SIGN_IN 인데 여기서 게임으로 잘못 잡혔음.
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build()
            // 구글 인증 화면으로 이동하는 코드
            val signInIntent = GoogleSignIn.getClient(this,gso).signInIntent
            // 후처리 함수 동작 연결시키기.
            requestLauncher.launch(signInIntent)

        }

        // 로그아웃
        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            MyApplication.email = null
            chageVisi("logout")
        }

        //test : 이메일 인증 후, 인증 디비에 저장하기.
        binding.signInBtn2.setOnClickListener {
            executeLoginAction()
            val email = binding.authEmailEdit.text.toString()
            val password = binding.authPasswordEdit.text.toString()

            if(!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                // createUserWithEmailAndPassword 이용해서, 입력한 이메일, 패스워드로 가입하기.
                MyApplication.auth.createUserWithEmailAndPassword(email,password)
                    // 회원가입이 잘 되었을 경우, 호출되는 콜백함수임.
                    .addOnCompleteListener(this) {
                            task ->
                        binding.authEmailEdit.text.clear()
                        binding.authPasswordEdit.text.clear()
                        if(task.isSuccessful) {
                            // 회원 가입 성공 한 경우,
                            MyApplication.auth.currentUser?.sendEmailVerification()
                                // 회원가입한 이메일에 인증 링크를 잘보냈다면, 수항해라 콜백함수.
                                ?.addOnCompleteListener { sendTask ->
                                    if (sendTask.isSuccessful) {
                                        Toast.makeText(
                                            this,
                                            "회원가입 성공, 전송된 메일 확인해주세요.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        chageVisi("logout")
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "메일 발송 실패",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        chageVisi("logout")
                                    }

                                }
                        }else {
                            // 회원 가입 실패 한 경우,
                            Toast.makeText(this,"회원가입 실패",Toast.LENGTH_SHORT).show()
                            chageVisi("logout")
                        }
                    }
            } else {
                Toast.makeText(this@AuthActivity,"계정 정보 확인 해주세요",Toast.LENGTH_SHORT).show()
            }


        }

        //이메일/비밀번호 기능 이용하기, 파이어베이스 인증 기능임.
        // 실제로 인증 링크를 받을수 있는 메일로 테스트
        // 여기서 사용하는 패스워드는, 현재 로그인하기위한, 패스워드
        // 파이어 베이스 인증, 해당 메일/ 등록한 패스워드로 등록이되고.
        // 인증해서, 로그인 이 가능함. android:text="가입"
//        binding.signInBtn2.setOnClickListener {
//
//            val email = binding.authEmailEdit.text.toString()
//            val password = binding.authPasswordEdit.text.toString()
//
//            if(!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
//                // createUserWithEmailAndPassword 이용해서, 입력한 이메일, 패스워드로 가입하기.
//                MyApplication.auth.createUserWithEmailAndPassword(email,password)
//                    // 회원가입이 잘 되었을 경우, 호출되는 콜백함수임.
//                    .addOnCompleteListener(this) {
//                            task ->
//                        binding.authEmailEdit.text.clear()
//                        binding.authPasswordEdit.text.clear()
//                        if(task.isSuccessful) {
//                            // 회원 가입 성공 한 경우,
//                            MyApplication.auth.currentUser?.sendEmailVerification()
//                                // 회원가입한 이메일에 인증 링크를 잘보냈다면, 수항해라 콜백함수.
//                                ?.addOnCompleteListener { sendTask ->
//                                    if (sendTask.isSuccessful) {
//                                        Toast.makeText(
//                                            this,
//                                            "회원가입 성공, 전송된 메일 확인해주세요.",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        chageVisi("logout")
//                                    } else {
//                                        Toast.makeText(
//                                            this,
//                                            "메일 발송 실패",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        chageVisi("logout")
//                                    }
//
//                                }
//                        }else {
//                            // 회원 가입 실패 한 경우,
//                            Toast.makeText(this,"회원가입 실패",Toast.LENGTH_SHORT).show()
//                            chageVisi("logout")
//                        }
//                    }
//            } else {
//                Toast.makeText(this@AuthActivity,"계정 정보 확인 해주세요",Toast.LENGTH_SHORT).show()
//            }
//
//
//        }

        // 가입한 이메일/패스워드로 , 로그인
        binding.logInBtn.setOnClickListener {
            val email = binding.authEmailEdit.text.toString()
            val password = binding.authPasswordEdit.text.toString()

            if(!email.isNullOrEmpty() && !password.isNullOrEmpty()) {

                MyApplication.auth.signInWithEmailAndPassword(email,password)
                    // 로그인 잘 되었을 경우, 실행될 콜백함수 등록.
                    .addOnCompleteListener(this) {
                            task ->
                        binding.authEmailEdit.text.clear()
                        binding.authPasswordEdit.text.clear()
                        // 로그인이 된경우
                        if(task.isSuccessful) {
                            // 로그인이 되었을 때, 해당 인증 확인.
                            // 현재 유저 확인, 했을 경우,
                            if(MyApplication.checkAuth()){
                                MyApplication.email = email
                                chageVisi("login")
                                val intent = Intent(this@AuthActivity, MainImageShareAppActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this,"전송된 메일로 인증이 안되었습니다",
                                    Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // 로그인이 안 된 경우
                            Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this@AuthActivity,"계정 정보 확인 해주세요",Toast.LENGTH_SHORT).show()
            }


        }

        //회원탈퇴
        binding.deleteUserBtn.setOnClickListener {
            showDialog()

        }

     // onCreate
    }

    private fun initializeRecaptchaClient() {
        lifecycleScope.launch {
            // BuildConfigField 값 호출
            val CAPCHA_API_KEY = BuildConfig.C_API_KEY
            Recaptcha.getClient(application, CAPCHA_API_KEY)
                .onSuccess { client ->
                    recaptchaClient = client
                    Log.d("lsy","캡챠 확인 성공")
                }
                .onFailure { exception ->
                    // Handle communication errors ...
                    // See "Handle communication errors" section
                }
        }
    }

    private fun executeLoginAction() {
        lifecycleScope.launch {
            recaptchaClient
                .execute(RecaptchaAction.LOGIN)
                .onSuccess { token ->
                    // Handle success ...
                    // See "What's next" section for instructions
                    // about handling tokens.
                    Log.d("lsy","캡챠 확인 성공2")
                }
                .onFailure { exception ->
                    // Handle communication errors ...
                    // See "Handle communication errors" section
                }
        }
    }

    private fun executeRedeemAction(){
        lifecycleScope.launch {
            recaptchaClient
                .execute(RecaptchaAction.custom("redeem"))
                .onSuccess { token ->
                    // Handle success ...
                    // See "What's next" section for instructions
                    // about handling tokens.
                }
                .onFailure { exception ->
                    // Handle communication errors ...
                    // See "Handle communication errors" section
                }
        }
    }
    private fun showDialog(){
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle("회원탈퇴")
            .setMessage("회원탈퇴 할까요?")
            .setIcon(R.drawable.ic_launcher_foreground)
            .setPositiveButton("YES") { dialog , which ->
                // 기능구현
                val user = MyApplication.auth.currentUser!!

                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this,"회원 탈퇴 성공",Toast.LENGTH_SHORT).show()
                            //회원 탈퇴후, 작성한 store 글 모두 삭제, 작성자로 검색해서 해당 문서 다가져오기.
                            db.collection("AndroidImageShareApp")
                                .whereEqualTo("email", MyApplication.email)
                                .get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        Log.d("lsy", "${document.id} => ${document.data}")
                                        // 각 문서 삭제
                                        db.collection("AndroidImageShareApp").document(document.id)
                                            .delete()
                                            .addOnSuccessListener { Log.d("lsy", "DocumentSnapshot successfully deleted!") }
                                            .addOnFailureListener { e -> Log.w("lsy", "Error deleting document", e) }

                                        // 회원 탈퇴후, 해당 이미지 모두 지우기.
                                        // Create a storage reference from our app
                                        val storageRef = storage.reference

// Create a reference to the file to delete
                                        val desertRef = storageRef.child("AndroidImageShareApp/${document.id}.jpg")

// Delete the file
                                        desertRef.delete().addOnSuccessListener {
                                            // File deleted successfully
                                        }.addOnFailureListener {
                                            // Uh-oh, an error occurred!
                                        }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("lsy", "Error getting documents: ", exception)
                                }




                            val intent = Intent(this@AuthActivity, MainImageShareAppActivity::class.java)
                            startActivity(intent)
                            finish()

                        }
                    }

            }.setNegativeButton("NO") {dialog, which ->

            }

            .create()
            .show()
    }

    //임의의 함수를 만들기.
    // 인증의 상태에 따라서, 로그인 화면을 표시 여부.
    // 예) 로그인이 되면, 로그아웃 버튼을 보이고,
    // 예) 로그인이 안되면, 로그아웃 버튼을 사라지게 만들기. 등
    fun chageVisi(mode: String) {
        if (mode === "login") {
            // 로그인이 되었다면, 인증된 이메일도 이미 등록이 되어서, 가져와서 사용하기.
            binding.authMainText.text = "${MyApplication.email} 님 반가워요."
            binding.logoutBtn.visibility = View.VISIBLE
            binding.deleteUserBtn.visibility = View.VISIBLE
            // 그외 버튼, 에디트 텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility = View.GONE
            binding.googleAuthInBtn.visibility = View.GONE
            binding.signInBtn2.visibility = View.GONE
            binding.authEmailEdit.visibility = View.GONE
            binding.authPasswordEdit.visibility = View.GONE
            binding.logInBtn.visibility = View.GONE
        } else if( mode === "logout") {
            binding.authMainText.text ="로그인 하거나 회원가입 해주세요."
            binding.logoutBtn.visibility = View.GONE
            // 그외 버튼, 에디트 텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility = View.GONE
            binding.deleteUserBtn.visibility = View.GONE
            binding.signInBtn2.visibility = View.VISIBLE
            binding.googleAuthInBtn.visibility = View.GONE
            binding.authEmailEdit.visibility = View.VISIBLE
            binding.authPasswordEdit.visibility = View.VISIBLE
            binding.logInBtn.visibility = View.VISIBLE

        } else if( mode === "signIn") {
            binding.logoutBtn.visibility = View.GONE
            // 그외 버튼, 에디트 텍스트뷰, 회원가입, 구글인증 다 안보이게 설정.
            binding.signInBtn.visibility = View.GONE
            binding.signInBtn2.visibility = View.GONE
            binding.googleAuthInBtn.visibility = View.GONE
            binding.authEmailEdit.visibility = View.VISIBLE
            binding.authPasswordEdit.visibility = View.VISIBLE
            binding.logInBtn.visibility = View.GONE

        }
    }


}







