function Annotator(options) {
    var self = this;
    self.annotations = options.annotations;


    var originX = -1;
    var originY = -1;
    var width = -1;
    var height = -1;
    var tr_latin = null;
    var tr_arabic = null;
    var dragging = false;

    console.log('Init');

    var canvas = document.getElementById(options.canvasId);
    var context = canvas.getContext('2d');
    var imageObj = new Image();

    imageObj.onload = function () {
        $(canvas).attr({width: this.width, height: this.height});
        redraw();
    };
    imageObj.src = options.imageSource;


    $(canvas).mousedown(function (e) {
        var mouseX = e.pageX - this.offsetLeft;
        var mouseY = e.pageY - this.offsetTop;

        originX = mouseX;
        originY = mouseY;

        width = 0;
        height = 0;

        dragging = true;

        tr_latin = 'tr';
        tr_arabic = ' الف';

        redraw();
    });

    $(canvas).mousemove(function (e) {
        if (dragging) {
            var mouseX = e.pageX - this.offsetLeft;
            var mouseY = e.pageY - this.offsetTop;

            width = mouseX - originX;
            height = mouseY - originY;

            redraw();
        }
    });

    $(canvas).mouseup(function (e) {
        if (dragging) {
            // origin points must be positive
            // width can be negative if dragging is done from left to right, but it cannot be 0
            if (originX >= 0 && originY >= 0 && width != 0 && height != 0) {
                var textData = {
                    'tr_latin': tr_latin,
                    'tr_arabic': tr_arabic
                };
                options.onAnnotationCreate(originX, originY, width, height, textData);
            }
        }
        dragging = false;
    });

    self.clearCurrent = function(){
        originX = -1;
        originY = -1;
        width = 0;
        height = 0;
        dragging = false;
    };

    self.addAnnotationFromCurrentOne = function (annotationId) {
        self.annotations.push(
            {
                'id' : annotationId,
                'x': originX,
                'y': originY,
                'w': width,
                'h': height,
                'textData': {
                    'tr_latin': tr_latin,
                    'tr_arabic': tr_arabic
                }
            }
        );

        self.clearCurrent();

        redraw();
    };

    function redraw() {
        canvas.width = canvas.width; // Clears the canvas
        context.drawImage(imageObj, 0, 0);

        //draw current selection (still dragging or dragging done), if any
        if (originX >= 0 && originY >= 0) {
            context.strokeStyle = "#FF0000";
            context.beginPath();
            context.rect(originX, originY, width, height);
            context.lineWidth = 3;
            context.stroke();
        }

        // draw other annotations
        for (var i = 0; i < self.annotations.length; i++) {
            var annotation = self.annotations[i];
            context.strokeStyle = "#0000FF";
            context.beginPath();
            context.rect(annotation.x, annotation.y, annotation.w, annotation.h);
            context.lineWidth = 1;
            context.stroke();
        }
    }


    self.show = function () {

    };
}