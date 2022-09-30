package com.khoshrang.forexquiz.ui

import android.content.res.AssetManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.khoshrang.forexquiz.adapters.QuizAdapter
import com.khoshrang.forexquiz.data.DataStoreManager
import com.khoshrang.forexquiz.data.Test
import com.khoshrang.forexquiz.data.SharedViewModel
import com.khoshrang.forexquiz.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val model: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dataStoreManager = requireContext().let { DataStoreManager(it) }
        val pics = model.testPictures

        val items = ArrayList<Test>()
        val assetManager: AssetManager =
            requireContext().assets
        for (file in assetManager.list("")!!) {
            if (file.endsWith(".csv")) {
                val flowValue: String
                runBlocking(Dispatchers.IO) {
                    flowValue = dataStoreManager.getGrade(file).first()
                }
                items.add(Test(file, flowValue, pics.random()))
            }
        }


        val recycler_view = binding.homeRecycle
        recycler_view.setHasFixedSize(true)
        var adapter = QuizAdapter(items, model, requireContext(), false)
        recycler_view.adapter = adapter


//
//        for(test in items){
//
//            GlobalScope.launch(
//                Dispatchers.IO
//            ) {
//
//                dataStoreManager?.getGrade(test.test_name)?.catch { e ->
//                    e.printStackTrace()
//                }?.collect {
//                    withContext(Dispatchers.Main) {
//                        Log.e("777777777777777 - ",it)
//                        test.test_grade = it
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//            }


        val premiumObserver = Observer<Boolean> { premium ->
            adapter = QuizAdapter(items, model, requireContext(), premium)
            recycler_view.adapter = adapter

        }
        model.premium.observe(viewLifecycleOwner, premiumObserver)


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}