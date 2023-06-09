package com.example.dbfordodo.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dbfordodo.R
import com.example.dbfordodo.databinding.FragmentKorzinaBinding
import com.example.dbfordodo.dodoViewMadel.madelFactory.RoomViewModelFactory
import com.example.dbfordodo.dodoViewMadel.viewModel.RoomViewModel
import com.example.dbfordodo.madel.data.Constants
import com.example.dbfordodo.madel.data.OrderConnectionServer
import com.example.dbfordodo.view.HelperClass.DataBaseApplication
import com.example.dbfordodo.view.adapter.ListAdapters.OrderAdapter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class KorzinaFragment : Fragment() {

    private var _binding: FragmentKorzinaBinding? = null
    private val binding get() = _binding!!

    private var adapter = OrderAdapter()

    //Room View Model
    private val roomViewModel: RoomViewModel by activityViewModels {
        RoomViewModelFactory((requireActivity().application as DataBaseApplication).database.ingredientsDao(),
            (requireActivity().application as DataBaseApplication).database.ingredientProductsConnectionDao(),
            (requireActivity().application as DataBaseApplication).database.productsDao(),
            (requireActivity().application as DataBaseApplication).database.orderDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKorzinaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        roomViewModel.getOrderedAmountLiveData().observe(viewLifecycleOwner){
            Log.d("TESTING","$it")
            adapter.list = it
            onOrderClicked()
            navigateToHomeFragment()
            observe()
            orderedBusketObserve()

        }

//        val list = roomViewModel.getOrderedAmount()
//        list.observe(viewLifecycleOwner){
//        }

        adapter.deleteProduct = { productId ->
            roomViewModel.deleteOrder(productId)
        }

        adapter.updateAmount = { productId,amount ->
            roomViewModel.updateOrderAmount(amount, Constants.USER_ID,productId)
        }


        binding.rvKorzinaItems.adapter = adapter
    }
    private fun orderedBusketObserve() {
        val busketList = roomViewModel.getBusket(Constants.USER_ID)
        busketList?.observe(viewLifecycleOwner) { list ->
            if (list.isEmpty()){
                binding.rvKorzinaItems.visibility = View.GONE
                binding.korzinaCountItems.visibility = View.GONE
                binding.emptyContainer.visibility = View.VISIBLE
                binding.btnCreateOrder.visibility = View.GONE
            }else {
                binding.rvKorzinaItems.visibility = View.VISIBLE
                binding.korzinaCountItems.visibility = View.VISIBLE
                binding.emptyContainer.visibility = View.GONE
                binding.btnCreateOrder.visibility = View.VISIBLE
            }
            adapter.submitList(list)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        roomViewModel.getProductsSum().observe(viewLifecycleOwner) {
            binding.korzinaCountItems.text = "${adapter.currentList.size} товара на $it TJS"
        }
    }

    private fun navigateToHomeFragment(){
        binding.korzinaEmptyBtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_korzinaFragment)
        }
    }

    private fun onOrderClicked() {
        binding.btnCreateOrder.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val newOrderServerList = mutableListOf<OrderConnectionServer>()
                roomViewModel.getOrderByUserId(Constants.USER_ID).forEach {
                    newOrderServerList.add(OrderConnectionServer(userId = it.userId, productId = it.productId, amount = it.amount))
                }
                roomViewModel.newOrderServer(newOrderServerList)
                roomViewModel.deleteOrderByUserId(Constants.USER_ID)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}