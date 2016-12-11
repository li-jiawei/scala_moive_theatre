/**
 * 
 */



var pageNum = 1;
function checkPage(){
	
}

function getPageData(){
	$.get("getpagedata?page="+pageNum,function(data){
		data = JSON.parse(data);
		var totalPage  = data.totalPage;
		var data = data.notes;
		var htmlstr = '';
		var pageHtmlStr='';
		if(data && data.length > 0){
			for(var i=0;i<data.length;i++){
				htmlstr += generateHtml(data[i])
			}
			$("#content").html(htmlstr);
			
			pageHtmlStr=generatePageNum(totalPage);
				
			
			$("#pageNumber").html(pageHtmlStr);
		}	
	});
}

function generatePageNum(PageNumber){
	var pagehtml='';
	pagehtml+='<li><a href="#">«</a></li>';
	for(var i=1;i<=PageNumber;i++){
		pagehtml+='<li><a href="#">'+i+'</a></li>';
	}
	pagehtml+='<li><a href="#">»</a></li>';

	return pagehtml;
	
	
}
function generateHtml(note){
	var html = '';
	html += '<tr><td><div class="col-md-2"><a href="viewnote?id="><img class="img-rounded img-responsive" src="'+note.cover +'"/></a></div>';
	html += '<div class="col-md-10"> <div class="row"><h4> <a href="viewnote?id='+note.id+'">'+note.title+'</a></h4></div>';
	html += '<div style="height:50px; overflow:hidden;word-break: break-all;word-wrap: break-word;" class="row">'+note.content+'</div>';
	html += '<div class="row">  <div class="col-md-3"> <span>'+ note.destination+'</span></div>';
	html += '<div class="col-md-1"> <img alt="ggg" style="width:20px;" class="img-rounded img-responsive" src="'+note.author.head+'"/></div>';
    html += '<div class="col-md-1">'+note.author.username+'</div> <div class="col-md-3"> <span><i></i>'+note.likecount+'</span> </div> </div>';
        	
	return html;
}


function nextPage(){
	pageNum ++;
	checkPage();
	getPageData();
}


function turnToPage(index){
	pageNum = index;
	checkPage();
	getPageData();
}


checkPage();
getPageData(); 

	