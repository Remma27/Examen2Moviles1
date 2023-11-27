import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.a14_firebaseaccess.R
import com.example.a14_firebaseaccess.entities.cls_Product

class ProductAdapter
    (context: Context, dataModalArrayList: ArrayList<cls_Product?>?) :
    ArrayAdapter<cls_Product?>(context, 0, dataModalArrayList!!) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listitemView = convertView
        if (listitemView == null) {
            listitemView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        }

        val dataModal: cls_Product? = getItem(position)

        val ProductID = listitemView!!.findViewById<TextView>(R.id.ProductID)
        val ProductName = listitemView!!.findViewById<TextView>(R.id.ProductName)
        val QuantityPerUnit = listitemView.findViewById<TextView>(R.id.QuantityPerUnit)
        val UnitPrice = listitemView.findViewById<TextView>(R.id.UnitPrice)


        if (dataModal != null) {
            ProductID?.text = dataModal.ProductID ?: "N/A"
            ProductName?.text = dataModal.ProductName ?: "N/A"
            QuantityPerUnit?.text = dataModal.QuantityPerUnit ?: "N/A"
            UnitPrice?.text = dataModal.UnitPrice ?: "N/A"
        }


        listitemView.setOnClickListener { // on the item click on our list view.
            // we are displaying a toast message.
            if (dataModal != null) {
                Toast.makeText(context, "Item clicked is : " + dataModal.ProductName, Toast.LENGTH_SHORT).show()
            }
        }
        return listitemView
    }
}
