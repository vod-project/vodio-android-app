package app.vodio.com.vodio.fragments.utils


interface CollectionView<T>{
    fun showCollectionLoading()
    fun showCollection(vods : List<T>)
    fun showCollectionError(message : String)
    fun showCollectionNoData()
}