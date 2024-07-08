package com.ahmedwagdy.testapi.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedwagdy.testapi.R
import com.ahmedwagdy.testapi.data.remote.ApiClient
import com.ahmedwagdy.testapi.data.remote.ApiResponse
import com.ahmedwagdy.testapi.data.remote.ApiResponseCallback
import com.ahmedwagdy.testapi.data.remote.DefaultApiClient
import com.ahmedwagdy.testapi.data.remote.RequestMethod
import com.ahmedwagdy.testapi.databinding.ActivityMainBinding
import com.ahmedwagdy.testapi.ui.data.HeaderItem

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity_Test_API"
    val testUrl = "https://jsonplaceholder.typicode.com/posts"
    var selectedRequestMethod = ""
    private val headerList = mutableListOf<HeaderItem>()
    private val headersAdapter: HeaderAdapter by lazy{
        HeaderAdapter(headerList)
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    private fun initView(){
        binding.requestBtn.setOnClickListener {
            testApiCalling()
        }
        initRequestMethodSpinnerView()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        binding.headersRv.layoutManager = LinearLayoutManager(this)
        binding.headersRv.adapter = headersAdapter
    }

    private fun initRequestMethodSpinnerView(){
        val spinnerItems = RequestMethod.entries.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.requestMethodSpinner.adapter = adapter
        val valueToSelect = RequestMethod.GET.name
        val position = adapter.getPosition(valueToSelect)
        binding.requestMethodSpinner.setSelection(position)
        binding.requestMethodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Handle the selected item
                selectedRequestMethod = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when no item is selected, if needed
                val selectedItem = parent.getItemAtPosition(0).toString()
                selectedRequestMethod = selectedItem
            }
        }
    }

    private fun getUrl():String{
        return binding.enterUrlEt.text.toString()
    }

    private fun testApiCalling() {
        val apiClient: ApiClient = DefaultApiClient()

// Example GET request
        apiClient.request(getUrl(),selectedRequestMethod, object : ApiResponseCallback {
            override fun onResponse(response: ApiResponse) {
                this@MainActivity.binding.responseValue.text = "Status Code = ${response.statusCode}\n Response Message: ${response.message}"
//                    Log.d(TAG, "onResponse: apiResponse = ${response.message} ")
                if (response.statusCode == 200) {
                    // Handle successful response
                    val apiResponse = response.message


                    // Process response here
                } else {
                    // Handle error response
                    val errorMessage = response.message
                    // Show error message
                }
            }
        })

// Example POST request
        val requestBody = "{\"key\":\"value\"}"
        apiClient.request("https://jsonplaceholder.typicode.com/posts",selectedRequestMethod, requestBody, object : ApiResponseCallback {
            override fun onResponse(response: ApiResponse) {
                // Handle response
                Log.d(TAG, "onResponse: apiResponse = ${response.message} ")
            }
        })

// Similar usage for other HTTP methods like PUT, DELETE, etc.

    }

}