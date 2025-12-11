app.filter("customDateTime",function (){
    return function(input) {
        if (!input) return '';
        // Split date and time parts
        var datePart = input.slice(0, 8);
        var timePart = input.slice(8);

        // Format date
        var year = datePart.slice(0, 4);
        var month = datePart.slice(4, 6);
        var day = datePart.slice(6, 8);

        // Format time
        var hours = timePart.slice(0, 2);
        var minutes = timePart.slice(2, 4);
        var seconds = timePart.slice(4, 6);

        // Return formatted string
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

    }
});