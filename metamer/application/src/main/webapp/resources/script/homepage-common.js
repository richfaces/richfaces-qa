function focusInput() {
	  var searchInput = document.getElementById('linksForm:componentSearch');
      if(searchInput!= null && searchInput.value!=null){
    	 if(searchInput.createTextRange){
    		 var range = serchInput.createTextRange;
    		 range.moveStart('character',serchInput.value.length);
    		 range.collapse().
    		 range.select();
    	 }
    	 else if(searchInput.selectionStart || searchInput.selectionStart == '0') {
             var elemLen = searchInput.value.length;
             searchInput.selectionStart = elemLen;
             searchInput.selectionEnd = elemLen;
             searchInput.focus();
    	 }
      }
      else if(searchInput != null){
    	  searchInput.focus();
      }
}