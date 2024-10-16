package com.kurbatov.appcbt.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kurbatov.appcbt.databinding.FragmentShopItemBinding
import com.kurbatov.appcbt.domain.ShopItem
import javax.inject.Inject

class ShopItemFragment : Fragment() {
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as CBTApplication).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement listener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        addChangeTextListeners()
        launchRightMode()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addChangeTextListeners() {
        binding.etSituation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.etThought.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        setCancelButton()
        binding.saveButton.setOnClickListener {
            Log.d("SaveButton", "Pressed in edit mode")
            viewModel.editShopItem(
                binding.textViewTime.text?.toString(),
                binding.etSituation.text?.toString(),
                binding.etEmotion.text?.toString(),
                binding.etThought.text?.toString(),
                binding.etSensations.text?.toString(),
                binding.etActions.text?.toString(),
                binding.etAnswer.text?.toString(),
                checkboxesToList()
            )
        }
    }

    private fun launchAddMode() {
        setCancelButton()
        binding.saveButton.setOnClickListener {
            Log.d("SaveButton", "Pressed in add mode")
            viewModel.addShopItem(
                binding.textViewTime.text?.toString(),
                binding.etSituation.text?.toString(),
                binding.etEmotion.text?.toString(),
                binding.etThought.text?.toString(),
                binding.etSensations.text?.toString(),
                binding.etActions.text?.toString(),
                binding.etAnswer.text?.toString(),
                checkboxesToList()
            )
        }
    }

    private fun setCancelButton(){
        binding.cancelButton.setOnClickListener {
            viewModel.finishWork()
        }
    }

    private fun checkboxesToList(): List<String> {
        val result = mutableListOf<String>()
        if (binding.checkboxOvergeneralization.isChecked)
            result.add(binding.checkboxOvergeneralization.text.toString())

        if (binding.checkboxAllOrNothing.isChecked)
            result.add(binding.checkboxAllOrNothing.text.toString())

        if (binding.checkboxNegativeFilter.isChecked)
            result.add(binding.checkboxNegativeFilter.text.toString())

        if (binding.checkboxDevaluationOfPositive.isChecked)
            result.add(binding.checkboxDevaluationOfPositive.text.toString())

        if (binding.checkboxConclusions.isChecked)
            result.add(binding.checkboxConclusions.text.toString())

        if (binding.checkboxExaggerationAndUnderstatement.isChecked)
            result.add(binding.checkboxExaggerationAndUnderstatement.text.toString())

        if (binding.checkboxEmotionalJustification.isChecked)
            result.add(binding.checkboxEmotionalJustification.text.toString())

        if (binding.checkboxObligation.isChecked)
            result.add(binding.checkboxObligation.text.toString())

        if (binding.checkboxLabels.isChecked)
            result.add(binding.checkboxLabels.text.toString())

        if (binding.checkboxPersonalization.isChecked)
            result.add(binding.checkboxPersonalization.text.toString())

        return result
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}