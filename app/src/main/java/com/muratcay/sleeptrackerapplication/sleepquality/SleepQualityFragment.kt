package com.muratcay.sleeptrackerapplication.sleepquality

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.muratcay.sleeptrackerapplication.R
import com.muratcay.sleeptrackerapplication.base.BaseFragment
import com.muratcay.sleeptrackerapplication.database.SleepDatabase
import com.muratcay.sleeptrackerapplication.databinding.FragmentSleepQualityBinding

class SleepQualityFragment : BaseFragment<FragmentSleepQualityBinding>() {
    private lateinit var viewModel: SleepQualityViewModel
    private val args: SleepQualityFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataSource = SleepDatabase.getInstance(requireContext()).sleepDatabaseDao

        val viewModelFactory = SleepQualityViewModelFactory(args.sleepNightKey, dataSource)

        viewModel = ViewModelProvider(this, viewModelFactory)[SleepQualityViewModel::class.java]

        binding.sleepQualityViewModel = viewModel

        binding.lifecycleOwner = this

        viewModel.navigateToSleepTracker.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())
                viewModel.doneNavigating()
            }
        }
    }

    override fun getFragmentView(): Int = R.layout.fragment_sleep_quality
}