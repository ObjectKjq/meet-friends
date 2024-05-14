export function UTCTime(time){
    let date = new Date(time)
    let year=date.getFullYear();
    let month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
    let day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
    return year+"-"+month+"-"+day;
}