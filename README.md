# Campus Dining Line Text Bot

  After conducting interviews, I concluded that a text messaging application for campus dining would be the most useful for Vanderbilt students. I came to this conclusion by conducting three interviews that focused on asking questions about three main campus categories: events, classes and dining. Although some of my interviewees found a lack of communication from Vanderbilt in the events and classes categories, I found that complaints about dining were consistent across all interviewees. 
  
  Specififcally, I am going to implement a text messaging application that Vanderbilt students can use to find out about wait times of different campus dining lines. The application will allow students to report and ask the specific wait times for dining lines on campus. Any student will be allowed to report line times, and any student will be able to ask about line times. When a student asks for a specific wait time, the application will send that student the most recent time reported. 
  
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
  - As a Vanderbilt student, I want to be able to ask for a wait time for a campus dining line, so that I can be efficient
    with my time and plan my meals accordingly. 
  -	As a Vanderbilt student, I want to be able to submit wait times for campus dining lines, so that I can help out my fellow
    peers and count on them to help me in the future.
  -	As a student, I want to be able to submit waiting times for the same line more than once or for different lines, so that I
    can provide accurate and helpful information.
  -	As a student, I want to receive a wait time that has the time stamp that time was submitted, so that I know how accurate
    the reported time is.
  -	As a student, I want to receive a wait time that has the closing time for that specific line, so that I know if I will be
    able to get food before the line closes. 

## Simplified List of Requirements:
  -	Any student can ask for a wait time for a campus dining line
  -	Any student can submit a wait time for a campus dining line
  -	Students are not limited to how many times they can submit times or ask for times
  -	Each wait time submission must record when that submission was made
  -	Each wait time submission must also include the closing time for that specific line 

 # Development Approach
 
The basic structure of the texting application has already been built; therefore, my developmental approach is specific to turning this basic texting application into a texting application that reports the waiting times for campus dining lines.

The way that the texting application currently works is that a user can interact with the application in two ways. The first way is that a user can register as an “expert” for a topic by texting “expert [insert specific topic]”. Once a user sends a text registering him or herself as an expert on that topic, their job then includes that they answer any questions that another user has specific to that topic. The second way the application works is that a user can ask questions about a specific topic and expect to receive answers to those questions if there are any experts registered for that topic. 

I plan on keeping this same structure when adapting the texting application to serve as a campus dining lines wait time texting application. The steps to adapting the texting application are:

## Implementation

## Step One: Update the keywords
     ### Motivation
      I want to change the keyword “expert” to be “report” so that it makes sense in the context of the   application.
      Therefore, when the application will know that a user is submitting a wait time if their submission starts with the word
      “report”.
    ### Implementation Step
     This step will involve replacing the word “expert” in the “queries” map to “report”
## Step Two: Update the string that the wait time reporter can submit 
    ### Motivation
     To report the line you are waiting on, the reporter can not only accept the topic which will be line name, but will also
     have to accept a string that tells the specific wait time. This means that the answer that the user wants for that
     specific line will be saved automatically, and will be the answer to the person asking. 
    ### Implementation Step
     This step will involve automatically saving the wait time, and concatenating it to the timestamp and its closing time as
     the “answer” set in the function answer-question. Therefore, I will have to update and simplify this function
## Step Three: Create the “answer” that is sent to the user who asked of the wait time
    ### Motivation
     This string should include the specific wait time recorded, concatenated to the time the wait time was recorded and when
     the line closes
    ### Implementation Step
     I will have to save the timestamp at the time the user submits a time stamp and concatenate it to the answer string. 
## Step Four: Research all the times the lines closes and varies by day
    ### Motivation
     Generate a time stamp that specifies the day of the week, not just the date. This is important because during the
     weekdays the lines are closed at different times than the weekend
    ### Implementation Step
     I will then have to transform the time stamp in order to also specify the day of the week, so that the closing time for
     that line is accurate. For example, on weekends, the lines close earlier. I plan on organizing the closing times by hard
     coding in the information into a nested map.
     
## Testing
1.	Test that a user can register for the same line with a different time
2.	Test that when the user asks the line time, the user gets an answer with no wait time
3.	Test that the time stamp sent is correct
4.	Test that the closing time is correct



