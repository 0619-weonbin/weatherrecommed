package com.example.weatherrecommender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}

@Composable
fun WeatherApp() {
    val weatherText = remember { mutableStateOf("날씨 정보를 불러오는 중...") }

    // Retrofit 설정
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherService = retrofit.create(WeatherApiService::class.java)
    val apiKey = "0" // 발급받은 API 키로 교체

    // API 호출
    weatherService.getWeather("Seoul", apiKey).enqueue(object : Callback<WeatherData> {
        override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
            if (response.isSuccessful) {
                val weatherData = response.body()
                val temp = weatherData?.main?.temp
                val description = weatherData?.weather?.get(0)?.description
                weatherText.value = "온도: $temp°C, 날씨: $description"
            } else {
                weatherText.value = "데이터를 가져오지 못했습니다."
            }
        }

        override fun onFailure(call: Call<WeatherData>, t: Throwable) {
            weatherText.value = "오류: ${t.message}"
        }
    })

    // UI 구성
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Text(
                text = weatherText.value,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
