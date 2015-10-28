$ ->
  $.get "/numbers", (numbers) ->
    $.each numbers, (index, number) ->
      $("#numbers").append $("<li>").text number.value
