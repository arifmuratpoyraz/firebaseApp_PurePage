package com.arifmuratpoyraz.purepage.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.arifmuratpoyraz.purepage.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

class BottomSheetFragment : BottomSheetDialogFragment(){

    private lateinit var bottomsheetProfile : Button
    private lateinit var bottomsheetShare : Button
    private lateinit var bottomsheetLogout : Button
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_fragment,container,false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        bottomsheetProfile = view.findViewById(R.id.bottomsheetProfile)
        bottomsheetShare = view.findViewById(R.id.bottomsheetShare)
        bottomsheetLogout = view.findViewById(R.id.bottomsheetLogout)

        bottomsheetProfile.setOnClickListener {
        val intent = Intent(activity, ProfileActivity::class.java)
            startActivity(intent)
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        bottomsheetShare.setOnClickListener {
            val intent = Intent(activity, ShareActivity::class.java)
            startActivity(intent)
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        bottomsheetLogout.setOnClickListener {
        auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}
