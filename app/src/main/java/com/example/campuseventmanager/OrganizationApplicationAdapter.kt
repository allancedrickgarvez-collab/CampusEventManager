package com.example.campuseventmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.campuseventmanager.databinding.ItemOrganizationApplicationBinding
import com.example.campuseventmanager.models.OrganizationApplication
import com.google.firebase.firestore.FirebaseFirestore

class OrganizationApplicationAdapter(
    private val context: Context,
    private var applications: List<OrganizationApplication>
) : RecyclerView.Adapter<OrganizationApplicationAdapter.ViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()

    inner class ViewHolder(val binding: ItemOrganizationApplicationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrganizationApplicationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val application = applications[position]
        holder.binding.orgNameTextView.text = application.orgName
        holder.binding.orgContactTextView.text = application.orgContact
        holder.binding.orgDocumentsTextView.text = application.orgDocuments

        holder.binding.approveButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Approve Application")
                .setMessage("Are you sure you want to approve this organization?")
                .setPositiveButton("Approve") { _, _ ->
                    approveApplication(application)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        holder.binding.rejectButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Reject Application")
                .setMessage("Are you sure you want to reject this organization?")
                .setPositiveButton("Reject") { _, _ ->
                    rejectApplication(application)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun getItemCount(): Int {
        return applications.size
    }

    fun updateApplications(newApplications: List<OrganizationApplication>) {
        applications = newApplications
        notifyDataSetChanged()
    }

    private fun approveApplication(application: OrganizationApplication) {
        firestore.collection("users").document(application.userId)
            .update("role", "organization")
            .addOnSuccessListener {
                deleteApplication(application)
                Toast.makeText(context, "Application Approved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun rejectApplication(application: OrganizationApplication) {
        deleteApplication(application)
        Toast.makeText(context, "Application Rejected", Toast.LENGTH_SHORT).show()
    }

    private fun deleteApplication(application: OrganizationApplication) {
        firestore.collection("orgApplications").document(application.id)
            .delete()
            .addOnSuccessListener {
                val mutableList = applications.toMutableList()
                mutableList.remove(application)
                updateApplications(mutableList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}