/**
 * Created by shou-quan.tang on 2018/5/22.
 * update by min-chong.liao on 2018/5/29.
 */

var g_FullHeight = document.documentElement.clientHeight

// 246 = 60(header) + 25(main padding) + 29(breadcrumb) + 60(form) + 10(main table margin) + 20(main table padding) + 42(table footer)
var g_TableHeight = g_FullHeight - 246

// 114 = 60(header) + 25(main padding) + 29(breadcrumb)
var g_TypesetMainHeight = g_FullHeight - 114

export function reSetGlobalHeight() {
  g_FullHeight = document.documentElement.clientHeight
  g_TableHeight = g_FullHeight - 246
  g_TypesetMainHeight = g_FullHeight - 114
}

// 整个页面高度
export function getFullHeight() {
  return g_FullHeight;
}

// 数据表格高度
export function getTableHeight() {
  return g_TableHeight;
}

// 排版区域高度
export function getTypesetMainHeight() {
  return g_TypesetMainHeight;
}
