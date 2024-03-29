package com.example.pontis.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pontis.R
import com.example.pontis.utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Display user's name
        val sharedPreferences = requireContext().getSharedPreferences(Constants.PONTIS_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"")!!
        val tvSettings: TextView = view.findViewById(R.id.tv_settings)
        tvSettings.text = "Hello $username"

        // Set onClickListener on Logout button, log user out and load SignInActivity
        val logout = view.findViewById<Button>(R.id.btn_logout)
        logout.setOnClickListener{
            // Retrieve details
            val context = requireContext().applicationContext
            val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(resId))
                .requestEmail()
                .build()
            // Sign user out
            val googlesigninclient = GoogleSignIn.getClient(requireActivity(), gso)
            googlesigninclient.signOut()
            FirebaseAuth.getInstance().signOut()
            // Load SignInActivity
            val intent = Intent(requireActivity(), SignInActivity::class.java)
            startActivity(intent)
        }

        // Set onClickListener for Change Details button, load ChangeDetailsActivity
        val changedetails = view.findViewById<Button>(R.id.changedetails)
        changedetails.setOnClickListener{
            val intent = Intent(requireActivity(), ChangeDetailsActivity::class.java)
            startActivity(intent)
        }

        // Set onClickListener for Delete Account button, delete user account and load SignInActivity
        val deleteaccount = view.findViewById<Button>(R.id.deleteaccount)
        deleteaccount.setOnClickListener{
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.delete()
            val intent = Intent(requireActivity(), SignInActivity::class.java)
            startActivity(intent)
            Toast.makeText(requireContext(), "Account deleted", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}