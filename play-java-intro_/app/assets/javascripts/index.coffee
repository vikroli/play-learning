$ ->
  $.get "/numbers", (numbers) ->
    $.each numbers, (index, number) ->
      id = $("<td>").text number.id
      value = $("<td>").text number.value
      $("#numbers").append $("<tr>").append(id, value)
