package com.imalat.beeSystem.view.imageView


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.imalat.beeSystem.R
import com.imalat.beeSystem.util.downloadFromUrl
import com.imalat.beeSystem.util.placeholderProgressBar
import kotlinx.android.synthetic.main.fragment_tam_ekran_resim.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_GELEN_FOTO = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [TamEkranResimFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TamEkranResimFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var gelenFotoAdres = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gelenFotoAdres = it.getString(ARG_GELEN_FOTO).toString()

        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireDialog().getWindow()!!.getAttributes().windowAnimations = R.style.sagdanGirSagdanCik
        return inflater.inflate(R.layout.fragment_tam_ekran_resim, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireDialog().getWindow()!!.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //requireDialog().getWindow()!!.clearFlags(WindowManager.LayoutParams.)

        Log.d("gelenFoto", gelenFotoAdres)

        kapatFragmenTamEkran.setOnClickListener {
            requireDialog().dismiss()
        }

        photo_view.downloadFromUrl(gelenFotoAdres, placeholderProgressBar(requireContext()))
        //image.downloadFromUrl(gelenFotoAdres, placeholderProgressBar(requireContext()))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TamEkranResimFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            TamEkranResimFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_GELEN_FOTO, param1)

                }
            }
    }


}