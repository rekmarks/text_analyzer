# text_analyzer

Java application for analysis of text files: letter frequency, word frequency, simple sentiment analysis, and quote length. Comes with a set of open source texts.

## Front Matter

#### Attributions

    Positive and negative words courtesy:
    
        Minqing Hu and Bing Liu. "Mining and Summarizing Customer Reviews." 
        Proceedings of the ACM SIGKDD International Conference on Knowledge 
        Discovery and Data Mining (KDD-2004), Aug 22-25, 2004, Seattle, 
        Washington, USA.
        

#### Regarding the Analysis

    Results can be found in "texts/analysis.txt". Running Analyze as-is will reproduce that
    file exactly.
    
    The sentiment-analysis would lead us to believe that the books are overwhelmingly positive.
    I suspect that's mainly for two reasons:
        1. The employed method can't deal with negations, e.g. "not good" only picks up "good", as
           positive.
        2. Probably most importantly, the word lists were created with social media (Twitter) in 
           mind, and are probably quite inaccurate in this context.


#### Regarding Testing

    Currently, Analyze is listed as the tester for all classes. More thorough and specific
    testing was accomplsihed during development by classes that have since been deleted.


## Classes

### analyzer

#### TextReader

    PURPOSE:            To read and encode text file representations of books and other
                        texts.
    
    FUNCTIONALITY:      Reads and stores text file as ArrayList (by lines) or a single 
                        String. Prunes (removes lines from) ArrayList representation if desired.
    
    DESIGN DECISIONS:   Attempted to keep functionality beyond reading as light as possible.
                        The addition of String representations for the purpose of quote 
                        analysis pushed it a little bit, but still pretty slim as the second-
                        shortest class in the package.
                        
    TESTER:             Analyze
    
    
#### LetterAnalyzer

    PURPOSE:            To perform letter frequency analysis on line-based representation
                        from TextReader.
                        
    FUNCTIONALITY:      Finds frequencies of all letters in the English alphabet in the text
                        and provides method for returning the 10 most frequent ones. 
                        
    DESIGN DECISIONS:   Decided to minimize user access to letter frequency counts, but trivial
                        to add back should it ever be useful.
                        
    TESTER:             Analyze
    

#### WordAnalyzer

    PURPOSE:            To perform word frequency and basic sentiment analysis on line-based
                        representation from TextReader.
                        
    FUNCTIONALITY:      Finds frequencies of all unique words in the text. Returns the 10 most
                        frequent words with or without reference to a user-specified stop list.
                        Performs basic (very basic) sentiment analysis by counting positive, 
                        negative, and neutral words with reference to positive and negative 
                        word lists.
                        
    DESIGN DECISIONS:   I originally had a separate class for sentiment analysis but decided to
                        implement it in WordAnalyzer as they required a lot of the same 
                        functionality (most importantly, generating a list of unique words with
                        frequencies), and the final result was still a pretty light class.
 
    TESTER:             Analyze
    
    
#### QuoteAnalyzer

    PURPOSE:            To perform quote length analysis on String-based representation from
                        TextReader.
    
    FUNCTIONALITY:      Takes the text as a single String from TextReader and uses regex with
                        Pattern, Matcher, and a find() while loop to grab matches. Can return
                        the top 10 longest and shortest quotes.
                        Assumptions and further information listed in findQuotes method comments.
                        
    DESIGN DECISIONS:   This took a long time to get right. The chosen method for quote matching
                        was the best (and only practicable) way to find quotes and deal with
                        apostrophe quote delimiters. Once I got it right, however, the method for
                        finding quotes became very simple. As with all other methods, very light
                        on functionality aside from what's necessary.
                        
    TESTER:             Analyze
    
    
#### TextAnalyzer

    PURPOSE:            To implement simple methods for running the full analysis suite (letter, 
                        word, sentiment, quotes) on texts, and to write the results to a file.
    
    FUNCTIONALITY:      Implements methods for each type of analysis, and then a single method
                        for calling all of them and printing their results in a legible format. 
                        
    DESIGN DECISIONS:   I determined early on that I would need some kind of wrapper to handle
                        output writing, output formatting, and to keep the logic of the ultimate
                        user interface simple. This class is that.
 
    TESTER:             Analyze
    

#### Analyze

    PURPOSE:            To provide a user interface for the analyzer package.
    
    FUNCTIONALITY:      Runs the full suite of analysis on user-defined text files and writes
                        results to file.
                        
    DESIGN DECISIONS:   Everything in this class was always going to go in main, and all previous
                        classes were designed so that Analyze's main could be kept simple.
                        
    TESTER:             n/a
