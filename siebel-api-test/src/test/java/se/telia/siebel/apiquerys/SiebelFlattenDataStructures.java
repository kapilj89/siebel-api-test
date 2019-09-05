package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.quote.data.ListOfQuoteItem;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.QuoteItem;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

public class SiebelFlattenDataStructures {

    // As stolen from C2B
    public static List<QuoteItem> getFlattenedQuoteItems(Quote quote) {
        return Optional.ofNullable(quote)
                .map(Quote::getListOfQuoteItem)
                .map(ListOfQuoteItem::getQuoteItem)
                .orElse(Collections.emptyList())
                .stream()
                .flatMap(it -> flatten(it, QuoteItem::getQuoteItem))
                .collect(Collectors.toList());
    }
    // As stolen from C2B
    public static <T> Stream<T> flatten(T obj, Function<T, List<T>> fun) {
        return Stream.concat(ofNullable(obj).map(Stream::of).orElse(Stream.empty()),
                ofNullable(obj).map(fun)
                        .orElse(emptyList())
                        .stream()
                        .flatMap(it -> flatten(it, fun)));
    }



}
