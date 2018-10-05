# Campus Dining Line Text Bot

  After conducting interviews, I concluded that a text messaging application for campus dining would be the most useful for Vanderbilt students. I came to this conclusion by conducting three interviews that focused on asking questions about three main campus categories: events, classes and dining. Although some of my interviewees found a lack of communication from Vanderbilt in the events and classes categories, I found that complaints about dining were consistent across all interviewees. 
  
  Specifically, I am going to implement a text messaging application that Vanderbilt students can use to find out about wait times of different campus dining lines. The application will allow students to report and ask the specific wait times for dining lines on campus. Any student will be allowed to report line times, and any student will be able to ask about line times. When a student asks for a specific wait time, the application will send that student the most recent time reported. 
  
  In conclusion, although this application seems simple, I believe it is one of high demand. Students often spend so much time in lines waiting for food that they are either late to class or skip meals because they do not want to be late for class. In addition, having a texting application that the entire Vanderbilt student body can contribute to is essential because knowing the wait times of different campus dining lines relies on time and place specific information that a network of friends often cannot provide. As a result, I believe this application will be an asset to Vanderbilt students and allow them to be more efficient with their time. 

# Questions:
 1.	What Vanderbilt portal do you use the most frequently? What do you like about it?
 2.	When you text your friends on campus, is it mostly to meet up for lunch, to study or do homework, to attend a club or
    event, or if other please specify?
 3.	What Vanderbilt-related information do you find yourself trying to look up the most frequently?
 4.	How do you find reviews about classes you want to take? Is it an easy or difficult task?
 5.	How do you find places on campus to study? Is it an easy or difficult task?
 6.	How do you find out about events on campus, such as club events, speakers or sports games? Do any or all of 
    these events seem under publicized?
 7.	How do you register for events on campus? Is it an easy or difficult task?
 8.	What do you do to find out about campus dining options? Is it an easy or difficult task?
 9.	Do you know when campus dining options open and close?
 10. Do you wait in campus dining lines often?
 11. What area of communication do you think needs to be improved the most: events, classes, or dining?
     
# Answers: 

## Question 1:
  - I use Brightspace the most often. I like that I can see my day to day activities.
  -	I use Brightspace. I like how all of my classes are all together.
  -	I use YES. I like how there are so many resources I have access to in one place. 
  
## Question 2:
  -	Lunch
  -	Lunch
  -	Study or do homework

## Question 3:
  -	Dining hall hours
  -	Dining times
  -	Course information

## Question 4:
  -	I ask in a large group chat. I find it easy.
  -	I ask my friends. I find it difficult sometimes.
  -	I ask my friends or look at Rate My Professor. I think of it as an easy task.

## Question 5:
  -	I walk around campus. It is hard to find a place that is quiet, but there are a lot of places to go.
  -	I find it very difficult.
  -	I walk around trying to find a place. It is hard to find a place if it's during midterms or after classes.

## Question 6:
  -	I find out about events through posters or friends. I think they are often under publicized.
  -	I find out about events through email, my sorority and friends. I think they are under publicized, and I wish there was a
    website I could refresh every day to see the events.
  -	I find out mainly through my sorority. I think a majority of the events are under publicized.

## Question 7:
  -	I usually search on Google the events or the speaker's name. I don’t think it is difficult.
  -	I don’t know how to register for events on campus. 
  -	I register for events at the Rand wall. I think it’s a good system.

## Question 8:
  -	I search on Google Vanderbilt dining all of the time. I find it difficult.
  -	I just go to the different places and check it out. I wish there was an easier way to search for these options.
  -	I use the Vanderbilt Menus application. It makes it easy. 

## Question 9:
  -	No, I always have to look it up.
  -	No
  -	No

## Question 10:
  -	Yes, all of the time. 
  -	Yes
  -	Yes

## Question 11:
  -	Campus dining
  -	Campus dining
  -	Campus dining 
 
 # Requirements
 
 ## Use cases
  - As a Vanderbilt student, I want to be able to ask for a campus dining line wait time, so that I can be efficient
    with my time and plan my meals accordingly. 
  -	As a Vanderbilt student, I want to be able to submit campus dining line wait times, so that I can help out my fellow
    peers and count on them to help me in the future.
  -	As a student, I want to be able to submit waiting times for the same line more than once or for different lines, so that I
    can provide accurate and helpful information.
  -	As a student, I want to receive a wait time that has a corresponding timestamp of when that time was recorded, so that I
    know how accurate the reported time is.
  -	As a student, I want to receive a wait time that has the corresponding closing time for that specific line, so that I know
    if I will be able to get food before the line closes. 

 ## Simplified List of Requirements
  -	Any student can ask for a wait time for a campus dining line
  -	Any student can submit a wait time for a campus dining line
  -	Students are not limited to how many times they can submit times or ask for times
  -	Each wait time submission must record when that submission was made and include it in the wait time response
  -	Each wait time response must include the closing time for that specific line 

 # Development Approach
 
The basic structure of the texting application has already been built; therefore, my developmental approach is specific to turning this basic texting application into a texting application that reports the waiting times for campus dining lines.

The way that the texting application currently works is that a user can interact with the application in two ways. The first way is that a user can register as an “expert” for a topic by texting “expert [insert specific topic]”. Once a user sends a text registering him or herself as an expert on that topic, his or her job includes that he or she answer any questions that another user has specific to that topic. The second way the application works is that a user can ask questions about a specific topic by texting "answer [insert specific topic] [insert question]", and expect to receive answers to those questions if there are any experts registered for that topic. 

I plan on keeping this same structure when adapting the texting application to serve as a campus dining lines wait time texting application. The basic structure of the texting application has been throughouly tested, so I am confident that the foundation of the application has no errors or bugs. 

## Implementation

1. Update the keywords

     Motivation:
     I want to change the keyword “expert” to be “report” so that it makes sense in the context of the application.
     
     Implementation Step:
     This will involve replacing the word “expert” in the “queries” map to “report”.
     
2. Update the string that the wait time reporter can submit 

     Motivation:
     To report, you will submit the line you are waiting on and the specific time for that line.
    
     Implementation Step: 
     This will updating the format of how information from reporters is parsed. Right now, the format is "report [topic]",
     where the topic can be the specific line. However, in addition to the line, the reporter has to record the wait time. As
     a result, this string needs to be updated to be sent and received as "report [topic] [time]". 
     
3. Create the “answer” that is sent to the user who asked of the wait time

     Motivation: 
     This string should include the specific wait time recorded, concatenated to the time the wait time was recorded and when
     the line closes
     
     Implementation Step: 
     This will involve saving the timestamp of when the user submits a timestamp and concatenate it to the answer string.
     
4. Research all the times the lines closes and varies by day

     Motivation: 
     Generate a timestamp that specifies the day of the week, not just the date. This is important because during the
     weekdays the lines are closed at different times than the weekend.
     
     Implementation Step: 
     This will involve transforming the timestamp in order to also specify the day of the week, so that the closing time for
     that line is accurate. I plan on organizing the closing times by hard coding in the information into a nested map.
     
## Testing

1.	Test that a user can register for the same line with a different time
2.	Test that when the user asks the wait time, the user gets the most recent report submission 
3.	Test that the time stamp sent is correct
4.	Test that the closing time is correct for every day of week
5.  Test that if a no time has every been registered for the line a user is asking for, they get a message saying so.

## Maintenance and Sustainability

Because the foundation of this application has been throughouly tested, I believe a majority of the adaptions will be easy to maintain and sustain. I believe that the part of the applicaiton that is not really sustainable is the hard coded map of the closing times of dining hall lines because there will be holidays and changes made to these schedules. Therefore, as a future improvement, I would like those closing times to eventually be recorded by scanning the online website for the times and saving them. 






