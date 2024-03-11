/* export */ function simpleMessage(msg) {
    console.log(msg)
}

/* export */ function complexMessage(msg) {
    console.log(new Date() + ": " + msg)
}

// 批量导出
// export { simpleMessage, complexMessage }

// 默认导出
export default { simpleMessage, complexMessage }