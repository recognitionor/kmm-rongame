package com.jhlee.kmm_rongame.android

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.SkuDetailsParams
import com.jhlee.kmm_rongame.core.data.androidTextToSpeech

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    lateinit var billingClient: BillingClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidTextToSpeech = TextToSpeech(this, this)
        billingClient = BillingClient.newBuilder(this)
            .setListener { billingResult, purchases ->
                purchasesUpdated(billingResult, purchases)
            }.enablePendingPurchases().build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Log.d("jhlee", "onBillingServiceDisconnected")
                // 연결 실패 시 재시도 로직을 구현.
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                Log.d("jhlee", "onBillingSetupFinished ${billingResult.responseCode}")
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // 준비 완료가 되면 상품 쿼리를 처리 할 수 있다!
                    queryProductDetails()
                }
            }
        })

//        setContent {
//            BackHandler {
//                if (backKeyListener == null) {
//                    finish()
//                } else {
//                    backKeyListener?.invoke()
//                }
//            }
//            App(AppModule(LocalContext.current))
//            ImageStorage.setContext(this)
//        }
    }

    private fun queryProductDetails() {
        Log.d("jhlee", "queryProductDetails")

        // 1. 조회할 상품 리스트 정의 (Product 객체 생성)
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("test") // 구글 플레이 콘솔에 등록된 실제 ID여야 함
                .setProductType(BillingClient.ProductType.INAPP) // 또는 SUBS
                .build(),
            // 필요한 만큼 추가
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        // 2. 비동기로 상품 정보 조회
        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d("jhlee", "상품 조회 성공: ${productDetailsList.size}개")

                // 테스트를 위해 조회된 첫 번째 상품으로 바로 결제 창을 띄워봅니다.
                // 실제로는 UI(버튼) 클릭 시 이 로직을 실행해야 합니다.
                if (productDetailsList.isNotEmpty()) {
                    launchPurchaseFlow(productDetailsList[0])
                }
            } else {
                Log.e("jhlee", "상품 조회 실패: ${billingResult.debugMessage}")
            }
        }
    }

    private fun launchPurchaseFlow(productDetails: ProductDetails) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val responseCode = billingClient.launchBillingFlow(this, billingFlowParams).responseCode
        Log.d("jhlee", "launchPurchaseFlow result: $responseCode")
    }

    private fun querySkuDetails() {
        Log.d("jhlee", "querySkuDetails")
        val skuList = ArrayList<String>()

        skuList.add("item_id_1")
        skuList.add("item_id_2")
        skuList.add("item_id_3")

        val params = SkuDetailsParams.newBuilder().apply {
            setSkusList(skuList)
            setType(BillingClient.SkuType.INAPP)        //정기 구독일 경우 BillingClient.SkuType.SUBS
        }.build()



        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            // 완료되면 SkuDetails(상품 상세 정보)를 List 형태로 반환한다.
            Log.d("jhlee", "querySkuDetailsAsync ${skuDetailsList?.size}")
            skuDetailsList?.forEach {
                Log.d("jhlee", "skuDetailsList?.forEach ${it.title}")
                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(it)
                    .build()

                val billingResult = billingClient.launchBillingFlow(
                    this,
                    flowParams
                )
            }


//launchBillingFlow()는 BillingResponseCode를 반환한다.
            if( billingResult.responseCode != BillingClient.BillingResponseCode.OK ) {
                //오류가 발생 할 경우 여기서 처리
            }
        }
    }

    private fun purchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                //구매 성공 시 처리
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // 사용자가 구매를 취소했을 경우 처리
        } else {
            // 이외의 오류 처리
        }
    }

    override fun onInit(status: Int) {
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
