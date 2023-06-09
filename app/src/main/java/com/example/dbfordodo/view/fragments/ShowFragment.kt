package com.example.dbfordodo.view.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.contains
import androidx.core.view.isGone
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.dbfordodo.R
import com.example.dbfordodo.databinding.ChipItemBinding
import com.example.dbfordodo.databinding.ChipTextItemBinding
import com.example.dbfordodo.databinding.DeleteIngridientItemBinding
import com.example.dbfordodo.databinding.ViewShowFragmentBinding
import com.example.dbfordodo.madel.data.Constants

import com.example.dbfordodo.dodoViewMadel.viewModel.DodoViewMadel
import com.example.dbfordodo.dodoViewMadel.madelFactory.DodoMadelFactory
import com.example.dbfordodo.dodoViewMadel.viewModel.RoomViewModel
import com.example.dbfordodo.dodoViewMadel.madelFactory.RoomViewModelFactory
import com.example.dbfordodo.view.HelperClass.DataBaseApplication
import com.example.dbfordodo.view.adapter.ListAdapters.ListSousAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import islom.din.dodo_ilmhona_proskills.db.data.Pizza
import islom.din.dodo_ilmhona_proskills.db.data.Vkus


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ShowFragment : Fragment() {
    private var _pos: Int? = null
    private val pos get() = _pos!!

    private var _pizza: Pizza? = null
    private val pizza get() = _pizza!!

    private var idPizza = 0


    //    lateinit var view:DeleteIngridientItemBinding
    private var _binding: ViewShowFragmentBinding? = null
    lateinit var recycler: RecyclerView
    lateinit var adapter: ListSousAdapter


    private val dodoViewMadel: DodoViewMadel by activityViewModels {
        DodoMadelFactory(
            (requireActivity().application as DataBaseApplication).database.pizzaDao(),
            (requireActivity().application as DataBaseApplication).database.orderDao()
        )

    }
    //Room View Model
    private val roomViewModel: RoomViewModel by activityViewModels {
        RoomViewModelFactory(
            (requireActivity().application as DataBaseApplication).database.ingredientsDao(),
            (requireActivity().application as DataBaseApplication).database.ingredientProductsConnectionDao(),
            (requireActivity().application as DataBaseApplication).database.productsDao(),
            (requireActivity().application as DataBaseApplication).database.orderDao()
        )
    }

    val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _pizza = it.getParcelable(ARG_PARAM1)
            _pos = it.getInt(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        this._binding = ViewShowFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.priceAllBay.setOnClickListener {
            Log.d("MYTESTINGERROR", "$it")
            roomViewModel.newOrderConnection(Constants.USER_ID, idPizza, 1)
            Toast.makeText(requireContext(), "Added to busket", Toast.LENGTH_SHORT).show()
        }
        val binding = view.let { ViewShowFragmentBinding.bind(it) }
        binding.backButton.setOnClickListener(){
            Navigation.findNavController(view).popBackStack()
        }
       /* binding.toBacketButton.setOnClickListener(){
            val action=ShowFragmentDirections.actionShowFragment2ToBasketFragment()
            findNavController().navigate(action)
        }*/


        when (pizza.category) {
            Constants.PIZZA -> {
                binding.removeIngrelienites.setOnClickListener {
                    val view1 = setupChip(pizza)
                    MaterialAlertDialogBuilder(requireContext())
                        .setPositiveButton("DONE") { dialog, which: Int ->
                            dodoViewMadel.getIngridient(pizza.id).observe(viewLifecycleOwner) {
                                for (ingrident in it.withIndex())
                                    if (ingrident.index !in view1.chipGroup.checkedChipIds)
                                        it[ingrident.index].delete = 1
                                Log.d("MyERROR", "${pizza.about}")
                                dialog.dismiss()
                            }
                        }
                        .setNeutralButton("CLEAR") { dialog, which ->

                            dialog.dismiss()
                        }
                        .setView(view1.root)
                        .show()
                }
                binding.nameShowder.setOnClickListener {
                    val view2 = setupChip(pizza)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Убрать ингридиенты")

                        .setPositiveButton("ГОТОВО") { dialog, which: Int ->
                            dialog.dismiss()

                        }
                        .setNegativeButton("Сбросить") { dialog, which: Int ->
                            dialog.cancel()
                        }
                        .setView(view2.root)
                        .show()
                }
                setupChipFurst(pizza)
                getRecycler()
                onClickSmail()
                onClickBig()
                onClickNormal()
                onClickType()
                onClickClassic()
            }
            Constants.DESERTI -> {
                binding.apply {
                    recSous.visibility = View.INVISIBLE
                    linearLayout.visibility = View.INVISIBLE
                    removeIngrelienites.visibility = View.INVISIBLE
                    if (pizza.category == Constants.DESERTI)
                        linearLayout2.visibility = View.INVISIBLE
                    classic.text = pizza.things.toString()
                }
            }
            Constants.NAPITKI -> {

                binding.apply {
                    dodoViewMadel.getPizzaName(pizza.name).observe(viewLifecycleOwner) {

                        when (it.size) {
                            1 -> {
                                normal.width = MATCH_PARENT
                                linearLayout2.visibility = View.INVISIBLE
                                normal.text = "0.5"
                                removeIngrelienites.visibility = View.INVISIBLE
                                small.isGone = true
                                big.isGone = true
                            }
                            2 -> {
                                normal.text = "0.5"
                                small.isGone = true
                                removeIngrelienites.visibility = View.INVISIBLE
                                linearLayout2.visibility = View.INVISIBLE
                                big.text = "1"
                                onClickBig()
                                onClickNormal()
                            }
                            3 -> {
                                small.text = "0.33"
                                normal.text = "0.5"
                                big.text = "1"
                                removeIngrelienites.visibility = View.INVISIBLE
                                linearLayout2.visibility = View.INVISIBLE
                                recSous.visibility = View.INVISIBLE
                                onClickSmail()
                                onClickBig()
                                onClickNormal()
                            }
                        }


                    }
                }
            }
            Constants.ZAKUSKI -> {
                binding.apply {
                    when (pizza.things) {
                        0 -> {
                            normal.width = MATCH_PARENT
                            linearLayout2.visibility = View.INVISIBLE
                            normal.text = "Большая"
                            removeIngrelienites.visibility = View.INVISIBLE
                            small.isGone = true
                            big.isGone = true
                        }
                        else -> {
                            normal.width = MATCH_PARENT
                            linearLayout2.visibility = View.INVISIBLE
                            normal.text = pizza.things.toString() + " шт"
                            removeIngrelienites.visibility = View.INVISIBLE
                            small.isGone = true
                            big.isGone = true
                        }
                    }
                }
            }
            Constants.COMBO -> {

            }
            else -> {
                binding.apply {
                    recSous.visibility = View.INVISIBLE
                    linearLayout.visibility = View.INVISIBLE
                    removeIngrelienites.visibility = View.INVISIBLE
                    if (pizza.category == Constants.DESERTI)
                        linearLayout2.visibility = View.INVISIBLE
                    binding.classic
                }
            }
        }

        Glide
            .with(this)
            .load(pizza.image)
            .into(binding.imageShowOder)
        binding.nameShowder.text = pizza.name
        binding.description.text = pizza.name
        binding.priceAllBay.setBackgroundResource(R.color.secondaryDarkColor)
        idPizza=pizza.id

    }


    fun getRecycler() {
        recycler = binding.recSous
        adapter = ListSousAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)

        dodoViewMadel.getVkus(2).observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null


    }


    fun onClickSmail() {
        binding.small.setOnClickListener {

            dodoViewMadel.getPizzaNameWithSize(pizza.name, 1).observe(viewLifecycleOwner) {
                it.forEach {
                    idPizza=it.id

                    Glide
                        .with(this)
                        .load(it.image)
                        .into(binding.imageShowOder)
                    //  binding.imageShowOder.scaleType = ImageView.ScaleType.CENTER_CROP


                    with(binding) {
                        small.setBackgroundResource(R.drawable.backround_select)
                        big.setBackgroundResource(R.drawable.back_selcted)
                        normal.setBackgroundResource(R.drawable.back_selcted)
                        if (it.category == Constants.PIZZA) {
                            classic.setBackgroundResource(R.drawable.backround_select)
                            tonciy.setBackgroundResource(R.drawable.back_selcted)
                            priceAllBay.text = "В КОРЗИНУ ЗА " + it.price.toString() + " TJK"

                            tonciy.isGone = true
                            classic.width = ViewGroup.LayoutParams.MATCH_PARENT
                            dodoViewMadel.getVkus(1).observe(viewLifecycleOwner) {
                                adapter.submitList(it)
                                seleltList(1)

                            }
                        }
                    }
                }
            }

        }


    }

    fun onClickBig() {
        binding.big.setOnClickListener {
            dodoViewMadel.getPizzaNameWithSize(pizza.name, 3).observe(viewLifecycleOwner) {
                it.forEach {
                    idPizza=it.id
                    Glide
                        .with(this)
                        .load(it.image)
                        .into(binding.imageShowOder)
                    //  binding.imageShowOder.scaleType = ImageView.ScaleType.CENTER_CROP

                    with(binding) {
                        small.setBackgroundResource(R.drawable.back_selcted)
                        big.setBackgroundResource(R.drawable.backround_select)
                        normal.setBackgroundResource(R.drawable.back_selcted)
                        if (it.category == Constants.PIZZA) {
                            tonciy.setBackgroundResource(R.drawable.back_selcted)
                            // binding.imageShowOder.setImageResource(R.drawable.ingridient_1)

                            priceAllBay.text = "В КОРЗИНУ ЗА " + it.price.toString() + " TJK"


                            dodoViewMadel.getVkus(3).observe(viewLifecycleOwner) {
                                adapter.submitList(it)
                            }
                            binding.tonciy.isGone = false
                            seleltList(3)
                        }
                    }
                }
            }
        }
    }

    fun onClickNormal() {
        binding.normal.setOnClickListener {

            dodoViewMadel.getPizzaNameWithSize(pizza.name, 2).observe(viewLifecycleOwner) {
                it.forEach {
                    idPizza=it.id
                    Glide
                        .with(this)
                        .load(it.image)
                        .into(binding.imageShowOder)
                    //  binding.imageShowOder.scaleType = ImageView.ScaleType.CENTER_CROP

                    with(binding) {
                        small.setBackgroundResource(R.drawable.back_selcted)
                        big.setBackgroundResource(R.drawable.back_selcted)
                        normal.setBackgroundResource(R.drawable.backround_select)
                        priceAllBay.text = "В КОРЗИНУ ЗА " + it.price.toString() + " TJK"
                        if (it.category == Constants.PIZZA) {
                            tonciy.setBackgroundResource(R.drawable.back_selcted)
                            // binding.imageShowOder.setImageResource(R.drawable.ingridient_12)
                            dodoViewMadel.getVkus(2).observe(viewLifecycleOwner) {
                                adapter.submitList(it)
                            }
                            seleltList(2)
                            tonciy.isGone = false
                        }
                    }
                }
            }
        }
    }

        fun onClickType() {
            binding.tonciy.setOnClickListener {
                binding.tonciy.setBackgroundResource(R.drawable.backround_select)
                binding.classic.setBackgroundResource(R.drawable.back_selcted)
                dodoViewMadel.getVkus(2).observe(viewLifecycleOwner) {


                    adapter.submitList(it)
                }
            }
        }

        fun onClickClassic() {
            binding.classic.setOnClickListener {
                binding.classic.setBackgroundResource(R.drawable.backround_select)
                binding.tonciy.setBackgroundResource(R.drawable.back_selcted)
                dodoViewMadel.getVkus(2).observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }

        }

        fun onClickInfo() {
            binding.info.setOnClickListener {
            }
        }

        fun getSelectItem() {
            adapter.onClick = {
                val newList = mutableListOf<Vkus>()
                val periodicList = adapter.currentList

                for (index in periodicList.indices) {
                    if (index == it) {
                        val listes = periodicList[it].copy(select = 1)
                        newList.add(listes)
                    } else {
                        newList.add(periodicList[index])
                    }
                    adapter.submitList(newList)

                }
            }
        }


    private fun setupChip(pizza: Pizza): DeleteIngridientItemBinding {
        val view = DeleteIngridientItemBinding.inflate(layoutInflater)
        dodoViewMadel.getIngridient(pizza.id).observe(viewLifecycleOwner) { ingridients ->


            for (item in ingridients.withIndex()) {
                if (item.value.available == 1) {
                    val chip = createChp(item.value.name)
                    chip.id = item.index

                    if (view.chipGroup.contains(chip))
                        view.chipGroup.removeAllViews()
                    view.chipGroup.addView(chip)
                    chip.isChecked = !(item.value.delete==1)

                    ingridients.map {
                        if (it.delete != 1) {
                            chip.isGone
                        }
                    }

                    Log.d("MyERROR", "${chip.isChecked}")
                }
            }

            view.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
                val size = binding.descriptionShowder.size
                binding.descriptionShowder.removeAllViews()
                for (i in 0 until size) {
                    val chip = createChiptext(ingridients[i].name)
                    chip.id = i

                    if (i !in checkedIds) {
                        val spannable = SpannableString(chip.text)
                        spannable.setSpan(
                            StrikethroughSpan(),
                            0,
                            chip.text.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        chip.text = spannable
                    }

                    binding.descriptionShowder.addView(chip)
                }

            }

        }
        return view
    }

    private fun setupChipFurst(pizza: Pizza) {

        dodoViewMadel.getIngridient(pizza.id).observe(viewLifecycleOwner) {

            for (item in it.withIndex()) {
                val chip = createChiptext(item.value.name)
                chip.id = item.index
                binding.descriptionShowder.addView(chip)
            }
        }


    }

    private fun createChiptext(category: String): TextView {
        val textView = ChipTextItemBinding.inflate(layoutInflater).root
        textView.text = category
        return textView
    }

    private fun createChp(category: String): Chip {
        val chip = ChipItemBinding.inflate(layoutInflater).root
        chip.text = category
        return chip
    }

    fun seleltList(size: Int) {

        val newList = mutableListOf<Vkus>()
        dodoViewMadel.getVkus(size).observe(viewLifecycleOwner) {


            for (item in it) {
                newList.add(item.copy(select = 0))
            }

            adapter.submitList(newList)
        }
    }

    companion object {

        fun newInstance(param1: islom.din.dodo_ilmhona_proskills.db.data.Pizza, pos: Int) =
            ShowFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, pos)


                }

            }
    }
}








