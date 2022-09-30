package com.khoshrang.forexquiz.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.khoshrang.forexquiz.data.DataStoreManager
import com.khoshrang.forexquiz.R
import com.khoshrang.forexquiz.data.SharedViewModel
import com.khoshrang.forexquiz.databinding.FragmentSettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    lateinit var eshterak: TextView
    private val model: SharedViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            eshterak = malekBuy
            imageViewTelegram.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.telegramLink)))
                startActivity(intent)

            }

            imageViewWhatsapp.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.whatsAppLink)))
                startActivity(intent)

            }

            imageViewrating.setOnClickListener {
                val appUrl = "https://cafebazaar.ir/app/"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl + "com.khoshrang.forexquiz"))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setPackage("com.farsitel.bazaar")
                try {
                    startActivity(intent)
                } catch (ex: ActivityNotFoundException) {
                    intent.setPackage(null)
                    startActivity(intent)
                }
            }

            buy.setOnClickListener {
                eshterak.text = getString(R.string.premiumIsActive)
                model.premium.value = true
                //                        Log.e("purchaseEntity",purchaseEntity.toString())
                lifecycleScope.launch(Dispatchers.IO) {
                    val dataStoreManager = context?.let { DataStoreManager(it) }

                    dataStoreManager?.savetoDataStore(
                        true
                    )
                }
            }

        }

        val premiumObserver = Observer<Boolean> { premium ->

            if (premium) {
                eshterak.text = getString(R.string.premiumIsActive)
                eshterak.visibility = View.VISIBLE
                binding.buy.visibility = View.GONE
            } else {
                eshterak.visibility = View.GONE
                binding.buy.visibility = View.VISIBLE

            }

        }

        model.premium.observe(viewLifecycleOwner, premiumObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}