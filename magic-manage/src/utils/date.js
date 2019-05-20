function format(date) {
  var separator = "-";
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  if (month >= 1 && month <= 9) {
    month = "0" + month;
  }
  if (day >= 0 && day <= 9) {
    day = "0" + day;
  }
  return year + separator + month + separator + day;
}

// 返回在vue模板中的调用接口
export default {
  format: function (date) {
    return format(date)
  }
}
