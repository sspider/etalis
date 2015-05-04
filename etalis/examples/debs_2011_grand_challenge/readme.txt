Readme.txt | Description of the DEBS11 grand challenge implementation
Author: Ahmed K. Hafsi <ahmed@hafsi.org>

0 - SHORT DESCRIPTION OF THE CHALLENGE : (http://debschallenge.event-processing.org/index.php/upcoming-challenge)

A question is posted, during the validity time of the question, a user could either answer the question or ask for the Most Frequently Answered Answer (MFAA).
After answering the question, the user could cancel his answer.
After asking the MFAA, the user could answer and then he could cancel.

A scoring system is resumed in the following table :

%Score				Event
%5					Correct answer
%1					Correct answer after asking for the most frequent answer
%-5					Incorrect answer
%100				First who answered
%-50				Three answers incorrect without a correct answer in the middle
%500				Correct answers to 10 consecutive questions*
%500				Correct answers to 10 questions within 30 minutes* during late night hours (1:00 – 5:00)
%1000				Best daily score (may apply to multiple players)**
%1000				Most appearances in the daily top five within a month (may apply to multiple players)
%1000				Best weekly score, given every Sunday midnight (may apply to multiple players)

I - DESCRIPTION OF THE IMPLEMENTATION :

