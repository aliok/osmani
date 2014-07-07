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

    self.clearCurrent = function () {
        originX = -1;
        originY = -1;
        width = 0;
        height = 0;
        dragging = false;
    };

    self.addAnnotationFromCurrentOne = function (annotationId) {
        // make sure direction of the annotation is left to right and top to bottom
        if (width < 0) {
            originX = originX + width;
            width = -width;
        }

        if (height < 0) {
            originY = originY + height;
            height = -height;
        }

        self.annotations.push(
            {
                'id': annotationId,
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

        $('.annotationOutline[data-annotation-id=' + annotationId + ']').click();
    };

    function redraw() {
        canvas.width = canvas.width; // Clears the canvas
        $('#annotationDivs').html("");  // Clears it

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

            // draw the outline
            var annotationOutlineDiv = $("<div class='annotationOutline'></div>");
            annotationOutlineDiv.attr("data-annotation-id", annotation.id);

            annotationOutlineDiv.css("height", annotation.h);
            annotationOutlineDiv.css("left", annotation.x);
            annotationOutlineDiv.css("top", annotation.y);
            annotationOutlineDiv.css("width", annotation.w);

            // draw the overlay
            var annotationOverlayDiv = $(
                    "<div class='annotationOverlay'>" +
                    "<div class='text'>" +
                    "<span>TR Arabic:</span><span class='tr_arabic'></span>" +
                    "<br/>" +
                    "<span>TR Latin:</span><span class='tr_latin'></span>" +
                    "</div>" +
                    "</div>");
            annotationOverlayDiv.attr("data-annotation-id", annotation.id);
            annotationOverlayDiv.find('.tr_arabic').html(annotation.textData.tr_arabic);
            annotationOverlayDiv.find('.tr_latin').html(annotation.textData.tr_latin);

            annotationOverlayDiv.css("left", annotation.x);
            annotationOverlayDiv.css("top", annotation.y + annotation.h);
            annotationOverlayDiv.css("min-width", annotation.w);
            annotationOverlayDiv.css("width", "auto");
            annotationOverlayDiv.css("height", "auto");

            // create a wrapper for annotation overlay and outline
            var wrapper = $("<div></div>");

            wrapper.append(annotationOutlineDiv);
            wrapper.append(annotationOverlayDiv);

            $('#annotationDivs').append(wrapper);
        }

//        if(dragging){
//            $('#annotationDivs').find('.annotationOutline').hide();
//        }
    }

    $(document).on("mouseover", "div.annotationOutline", function (e) {
        var self = $(this);
        self.addClass('hover');
        self.siblings('.annotationOverlay').css('z-index', 5);
        self.siblings('.annotationOverlay').show();
    });

    $(document).on("mouseout", "div.annotationOutline", function (e) {
        var self = $(this);
        self.removeClass('hover');
        self.siblings('.annotationOverlay').css('z-index', 0);
        self.siblings('.annotationOverlay').hide();
    });

    $(document).on("click", "div.annotationOutline", function () {
        if (dragging)
            return false;
        var self = $(this);
        self.addClass('selected');
        self.siblings('.annotationOverlay').css('z-index', 5);
        self.siblings('.annotationOverlay').show();
        options.onAnnotationSelect(self.attr('data-annotation-id'));
    });


    self.show = function () {

    };
}