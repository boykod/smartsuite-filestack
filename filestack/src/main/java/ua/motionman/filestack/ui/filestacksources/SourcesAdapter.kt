package ua.motionman.filestack.ui.filestacksources

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.motionman.filestack.databinding.SourceItemBinding
import ua.motionman.filestack.databinding.SourceSectionBinding
import ua.motionman.filestack.domain.model.SourceModel
import ua.motionman.filestack.utils.delegate.viewBinding

typealias SourceSelected = (SourceModel.Source) -> Unit

class SourcesAdapter(private val onSelect: SourceSelected) :
    ListAdapter<SourceModel, RecyclerView.ViewHolder>(SourcesDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SECTION -> SourceSectionViewHolder(parent.viewBinding(SourceSectionBinding::inflate))
            VIEW_TYPE_SOURCES -> SourceViewHolder(parent.viewBinding(SourceItemBinding::inflate))
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SourceSectionViewHolder -> holder.bind(getItem(position) as SourceModel.Header)
            is SourceViewHolder -> holder.bind(getItem(position) as SourceModel.Source)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SourceModel.Header -> VIEW_TYPE_SECTION
            is SourceModel.Source -> VIEW_TYPE_SOURCES
        }
    }


    inner class SourceViewHolder(private val binding: SourceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SourceModel.Source) {
            with(binding) {
                root.setOnClickListener { onSelect(item) }
                sourceNameTextView.text = itemView.context.getText(item.name)
            }
        }
    }

    inner class SourceSectionViewHolder(private val binding: SourceSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(section: SourceModel.Header) {
            binding.sourceSectionTextView.text = itemView.context.getText(section.section)
        }
    }

    companion object {
        const val VIEW_TYPE_SECTION = 0
        const val VIEW_TYPE_SOURCES = 1
    }
}

class SourcesDiffCallback : DiffUtil.ItemCallback<SourceModel>() {
    override fun areItemsTheSame(oldItem: SourceModel, newItem: SourceModel): Boolean {
        return when {
            oldItem is SourceModel.Header && newItem is SourceModel.Header -> oldItem.section == newItem.section
            oldItem is SourceModel.Source && newItem is SourceModel.Source -> oldItem.type == newItem.type
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: SourceModel, newItem: SourceModel): Boolean {
        return when {
            oldItem is SourceModel.Header && newItem is SourceModel.Header -> oldItem.section == newItem.section
            oldItem is SourceModel.Source && newItem is SourceModel.Source -> {
                oldItem.type == newItem.type
            }
            else -> false
        }
    }
}
