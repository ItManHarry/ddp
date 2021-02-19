/**
 * 获取系统下拉选项
 * @param code - 字典代码
 * @param callback - 回调函数
 * @returns
 */
function getSelectItems(code, callback){
	alert('do select item function...')
	axios.get('/pm/sys/enum/options', {
	    params: {
	      code:'D004'
	    }
	}).then(function (response) {
		var result = response.data
		alert('Get items ok...')
		callback(result.data)
	}).catch(function (error) {
	    console.log(error)
	})
}