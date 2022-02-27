package com.muratcay.sleeptrackerapplication.sleepdetail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.muratcay.sleeptrackerapplication.R
import com.muratcay.sleeptrackerapplication.base.BaseFragment
import com.muratcay.sleeptrackerapplication.database.SleepDatabase
import com.muratcay.sleeptrackerapplication.databinding.FragmentSleepDetailBinding

class SleepDetailFragment : BaseFragment<FragmentSleepDetailBinding>() {

    private lateinit var viewModel: SleepDetailViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val arguments = SleepDetailFragmentArgs.fromBundle(requireArguments())

        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepDetailViewModelFactory(arguments.sleepNightKey, dataSource)

        viewModel = ViewModelProvider(this, viewModelFactory)[SleepDetailViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observeNavigateToSleepTracker()
    }

    private fun observeNavigateToSleepTracker() {
        viewModel.navigateToSleepTracker.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment())
                viewModel.doneNavigating()
            }
        }
    }

    override fun getFragmentView() = R.layout.fragment_sleep_detail
}