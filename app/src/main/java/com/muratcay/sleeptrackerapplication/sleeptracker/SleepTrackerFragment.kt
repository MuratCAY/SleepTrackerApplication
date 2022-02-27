package com.muratcay.sleeptrackerapplication.sleeptracker

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.muratcay.sleeptrackerapplication.R
import com.muratcay.sleeptrackerapplication.base.BaseFragment
import com.muratcay.sleeptrackerapplication.database.SleepDatabase
import com.muratcay.sleeptrackerapplication.databinding.FragmentSleepTrackerBinding

class SleepTrackerFragment : BaseFragment<FragmentSleepTrackerBinding>() {

    private lateinit var viewModel: SleepTrackerViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[SleepTrackerViewModel::class.java]

        binding.sleepTrackerViewModel = viewModel
        binding.lifecycleOwner = this

        observeNavigateToSleepQuality()
        observeClearShowingSnackBar()
        configureRecyclerView()
        observeNavigateToSleepDataQuality()
    }

    private fun observeNavigateToSleepDataQuality() {
        viewModel.navigateToSleepDataQuality.observe(viewLifecycleOwner) { night ->
            night?.let {
                findNavController().navigate(
                    SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepDetailFragment(
                        it
                    )
                )
                viewModel.onSleepDataQualityNavigated()
            }
        }
    }

    private fun configureRecyclerView() {

        val adapter = SleepNightAdapter(SleepNightListener { nightId ->
            viewModel.onSleepNightClicked(nightId)
        })

        binding.recyclerView.adapter = adapter

        viewModel.nights.observe(viewLifecycleOwner) {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        }

        val manager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.layoutManager = manager
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }
    }

    private fun observeClearShowingSnackBar() {
        viewModel.showSnackBarEvent.observe(viewLifecycleOwner) {
            if (it == true) {
                Snackbar.make(binding.root, R.string.cleared_message, Snackbar.LENGTH_SHORT).show()
                viewModel.doneShowingSnackBar()
            }
        }
    }

    private fun observeNavigateToSleepQuality() {
        viewModel.navigateToSleepQuality.observe(viewLifecycleOwner) { night ->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(
                        night.nightId
                    )
                )
                viewModel.doneNavigating()
            }
        }
    }

    override fun getFragmentView(): Int = R.layout.fragment_sleep_tracker
}