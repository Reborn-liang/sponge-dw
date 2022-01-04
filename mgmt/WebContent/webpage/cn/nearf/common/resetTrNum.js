//初始化下标
function resetTrNum(tableId) {
	$tbody = $("#"+tableId+"");
	$tbody.find('>tr').each(function(i){
		$(':input, select,button,a', this).each(function(){
			var $this = $(this), name = $this.attr('name');
			var id=$this.attr('id');
			var onclick_str=$this.attr('onclick');
			var val = $this.val(); 
			var onchange_str=$this.attr('onchange');
			var dataoptions_str=$this.attr('data-options');
			if(name!=null){
				if (name.indexOf("#index#") >= 0){
					$this.attr("name",name.replace('#index#',i));
				}else{
					var s = name.indexOf("[");
					var e = name.indexOf("]");
					var new_name = name.substring(s+1,e);
					$this.attr("name",name.replace(new_name,i));
				}
			}
			if(id!=null){
				if (id.indexOf("#index#") >= 0){
					$this.attr("id",id.replace('#index#',i));
				}else{
					var s = id.indexOf("[");
					var e = id.indexOf("]");
					var new_id = id.substring(s+1,e);
					$this.attr("id",id.replace(new_id,i));
				}
			}
			if(onclick_str!=null){
				if (onclick_str.indexOf("#index#") >= 0){
					$this.attr("onclick",onclick_str.replace(/#index#/g,i));
				}else{
					
				}
			}
			if(onchange_str!=null){
				if (onchange_str.indexOf("#index#") >= 0){
					$this.attr("onchange",onchange_str.replace(/#index#/g,i));
				}else{
					var s = onchange_str.indexOf("(");
					var e = onchange_str.indexOf(")");
					var new_change = onchange_str.substring(s+1,e);
					if((s+1)==e){	//表示括号内无内容，可直接跳过，不用替换.
						console.log("=== skip");
					}else{
						$this.attr("onchange",onchange_str.replace(new_change,i));
					}
				}
			}
			if(dataoptions_str!=null &&  dataoptions_str != undefined){
				if (dataoptions_str.indexOf("'#index#'") >= 0){
					$this.attr("data-options",dataoptions_str.replace(/'#index#'/g,i));
				}else{
					var s = dataoptions_str.indexOf("(");
					var e = dataoptions_str.indexOf(")");
					var new_change = dataoptions_str.substring(s+1,e);
					if((s+1)==e){	//表示括号内无内容，可直接跳过，不用替换.
						console.log("111=== skip");
					}else{
						$this.attr("data-options",dataoptions_str.replace(new_change,i));
					}
				}
			}
		});
		$(this).find('div[name=\'xh\']').html(i+1);
	});
}