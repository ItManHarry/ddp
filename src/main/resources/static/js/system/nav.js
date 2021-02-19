function getMenus(callback){
	alert('get menus...')
	//获取系统权限
	axios.get('/pm/biz/pro/group/members', {
	    params: {
	      proId:'111'		      		      
	    }
	}).then(function (response) {
		//alert('Get system menus successfully!!!')
//		var result = response.data
//		alert('Get items ok...')
//		alert(result.data)
		menus = [
			{name:'1',id:1},
			{name:'2',id:2},
			{name:'3',id:3},
			{name:'4',id:4}
		]
		callback(menus)
	}).catch(function (error) {
		alert("error")
	    console.log(error)
	})
}