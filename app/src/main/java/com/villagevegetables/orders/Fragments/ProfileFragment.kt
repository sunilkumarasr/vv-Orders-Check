package com.villagevegetables.orders.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.villagevegetables.orders.Activitys.MyAccountActivity
import com.villagevegetables.orders.R
import com.villagevegetables.orders.databinding.FragmentProfileBinding


class ProfileFragment : Fragment(),View.OnClickListener {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        binding.relativeMyAccount.setOnClickListener(this)
        binding.relativeLogout.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.relativeMyAccount -> {
                startActivity(Intent(requireActivity(), MyAccountActivity::class.java))
            }

            R.id.relativeLogout -> {
                LogoutDialog()
            }
        }
    }


    private fun LogoutDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_logout, null)
        bottomSheetDialog.setContentView(view)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)
        buttonCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        buttonOk.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

}