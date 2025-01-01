class MainActivity : AppCompatActivity() {
 	override fun onCreate(savedInstanceState: Bundle?) {
 		super.onCreate(savedInstanceState)
 		setContentView(R.layout.activity_main)
 		Log.d(TAG,"Ini ada di onCreate")
 	}
 
	override fun onResume() {
 		super.onResume()
 		Log.d(TAG,"Ini ada di onResume")
 	}

 	override fun onPause() {
 		super.onPause()
 		Log.d(TAG,"Ini ada di onPause")
 	}

 	override fun onStop() {
 		super.onStop()
 		Log.d(TAG,"Ini ada di onStop")
 	}

 	override fun onDestroy() {
 		super.onDestroy()
 		Log.d(TAG,"Ini ada di onDestroy")
 	}
}
