function getUrlParameterMap(){
	let url = location.href;
	let index = url.indexOf("?");
	let parameterPart = url.substr(index + 1);
	let parameters = parameterPart.split("&");
	let map = {};
	for(var i in parameters){
		let item = parameters[i].split('=')
		map[item[0]] = item[1];
	}
	return map;
}

function formatDate(time) {
	if(time!=null){
		var datetime = new Date();
		datetime.setTime(time);
		var year = datetime.getFullYear();
		var month = (datetime.getMonth() + 1)<10?"0"+(datetime.getMonth() + 1):(datetime.getMonth() + 1);
		var date = datetime.getDate()<10?"0"+datetime.getDate():datetime.getDate();
		var hour = datetime.getHours()<10?"0"+datetime.getHours():datetime.getHours();
		var minute = datetime.getMinutes()<10?"0"+datetime.getMinutes():datetime.getMinutes();
		var second = datetime.getSeconds()<10?"0"+datetime.getSeconds():datetime.getSeconds();
		return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
	}else{
		return "---";
	}
}