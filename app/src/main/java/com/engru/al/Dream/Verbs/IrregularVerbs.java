package com.engru.al.Dream.Verbs;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.engru.al.Dream.BaseORM;

public class IrregularVerbs extends BaseORM {
    int id;
    String infinitive_v1;
    String past_simple_v2;
    String past_particple_v3;
    String ru;
    String eng_value;
    String example;
    IrregularVerbs(){
        id=-1;
        infinitive_v1="";
        past_simple_v2="";
        past_particple_v3="";
        ru="";
        eng_value="";
        example="";
    }

    public static class Table{
        public static final String TABLE_NAME="IrregularVerbs";
        public static final String ID="_id";
        public static final String Infinitive_V1="Infinitive_V1";
        public static final String PAST_SIMPLE_V2="PAST_SIMPLE_V2";
        public static final String PAST_PARTICPLE_V3="PAST_PARTICPLE_V3";
        public static final String RU="ru";
        public static final String ENG_VALUE="eng_value";
        public static final String EXAMPLE="example";
        public static final String CREATE_TABLES="CREATE TABLE "+ TABLE_NAME + " ( "
                + " _id INTEGER PRIMARY KEY, "
                + Infinitive_V1 + " TEXT , "
                + PAST_SIMPLE_V2 + " TEXT, "
                + PAST_PARTICPLE_V3 + " TEXT, "
                + ENG_VALUE + " TEXT, "
                + EXAMPLE + " TEXT, "
                + RU + " TEXT "
                +")";
        public static final String INDEX_FOR_SORT_NAME="index_Infinitive_V1";
        public static final String INDEX_FOR_SORT_SQL=" CREATE INDEX " + INDEX_FOR_SORT_NAME +
                " ON "+TABLE_NAME+" ("+Infinitive_V1+");";
        public static void insertDefaultValues(SQLiteDatabase db){
            // 1 - Infinitive_V1
            // 2 - PAST_SIMPLE_V2
            // 3 - PAST_PARTICPLE_V3
            // 4 - RU
            // 5 - ENG_VALUE
            // 6 - EXAMPLE
            String [][]importantSomeWords={
                    {"be", "was, were", "been", "быть, являться" , " used to show the position of someone or something" , "It's been in the cupboard for months." },
                    {"beat", "beat", "beaten", "бить, колотить" , "to hit against something hard, making a continuous or regular sound" , "Rain beat against the windows." },
                    {"become", "became", "become", "становиться" , " to begin to be something" , "She wants to become a teacher when she leaves school." },
                    {"begin", "began", "begun", "начинать" , " to start to do something" , "She began her career as a journalist on a local newspaper." },
                    {"bend", "bent", "bent", "гнуть / сгибать(ся)" , "to move your body or part of your body so that it is not straight" , "He was bending over to tie his shoelaces." },
                    {"bet", "bet", "bet", "ставить, держать пари" , "to risk money on the result of a game, competition, etc" , "He lost all his money betting on horses." },
                    {"bite", "bit", "bitten", "кусать" , "to cut something using your teeth" , "She bit into an apple. | He was bitten by a dog." },
                    {"blow", "blew", "blown", "дуть, выдыхать" , "to force air out through your mouth" , "She blew on her coffee before taking a sip." },
                    {"break", "broke", "broken", "ломать, разбивать, разрушать" , "to separate into two or more pieces, or to make something separate into two or more pieces" , "The vase fell on the floor and broke." },
                    {"bring", "brought", "brought", "приносить, привозить, доставлять" , "to take someone or something with you when you go somewhere" , "Did you bring an umbrella with you?" },
                    {"build", "built", "built", "строить, сооружать" , "to make something by putting materials and parts together" , "The bridge is built of steel and aluminium." },
                    {"buy", "bought", "bought", "покупать, приобретать" , "to get something by paying money for it" , "I went out to buy some milk." },
                    {"catch", "caught", "caught", "ловить, поймать, схватить" , "to stop someone or something that is moving through the air by getting hold of it" , "She fell backwards but he caught her in his arms." },
                    {"choose", "chose", "chosen", "выбирать, избирать" , "to decide which thing you want" , "I helped my sister choose a name for her baby." },
                    {"come", "came", "come", "приходить, подходить" , "to arrive somewhere or go to a place" , "Can you come to my party?" },
                    {"cost", "cost", "cost", "стоить, обходиться" , "If something costs a particular amount of money, you have to pay that in order to buy or do it. | to make someone lose something" , "How much do these shoes cost? | His lazy attitude cost him his job." },
                    {"cut", "cut", "cut", "резать, разрезать" , "to use a knife or other sharp tool to divide something, remove part of something, or make a hole in something" , "He cut the piece of wood in half." },
                    {"deal", "dealt", "dealt", "иметь дело, распределять" , "to give or share out something, especially playing cards." , "Whose turn is it to deal?" },
                    {"dig", "dug", "dug", "копать, рыть" , "to break or move the ground with a tool, machine, etc" , "Digging the garden is good exercise." },
                    {"do", "did", "done", "делать, выполнять" , "used with another verb to form questions and negative phrases" , "Do you need any help?" },
                    {"draw", "drew", "drawn", "рисовать, чертить | привлекать" , "to produce a picture by making lines or marks, usually with a pen or pencil | to attract someone to a place or person" , "She drew a picture of a tree. | Thousands of tourists are drawn to the city every year." },
                    {"drink", "drank", "drunk", "пить" , "to put liquid into your mouth and swallow it" , "He was drinking a glass of milk." },
                    {"drive", "drove", "driven", "ездить, подвозить" , "to travel somewhere in a car, or to take someone somewhere in a car" , "My friend drove me home last night." },
                    {"eat", "ate", "eaten", "есть, поглощать, поедать" , "to put food into your mouth and then swallow it" , "I haven't eaten since breakfast." },
                    {"fall", "fell", "fallen", "падать" , "to move down towards the ground" , "Huge drops of rain were falling from the sky." },
                    {"feed", "fed", "fed", "кормить" , " to give food to a person, group, or animal" , "I fed Simone's cat while she was away." },
                    {"feel", "felt", "felt", "чувствовать, ощущать" , "to experience an emotion or a physical feeling" , "You shouldn't feel embarrassed about making a mistake." },
                    {"fight", "fought", "fought", "драться, сражаться, воевать" , "use physical force to try to defeat each other." , "Two men were arrested for fighting outside a bar." },
                    {"find", "found", "found", "находить, обнаруживать" , "to discover something or someone that you have been searching for" , "I can't find my glasses and I've looked everywhere." },
                    {"fly", "flew", "flown", "летать" , "to travel through the air" , "The plane was flying at 5000 feet." },
                    {"forget", "forgot", "forgotten", "забывать о (чём-либо)" , "to be unable to remember a fact, something that happened, or how to do something" , "I've forgotten his name." },
                    {"forgive", "forgave", "forgiven", "прощать" , "to decide not to be angry with someone or not to punish them for something they have done" , "I've apologized, but I don't think she'll ever forgive me." },
                    {"freeze", "froze", "frozen", "замерзать, замирать" , "becomes hard and solid because it is very cold | to feel very cold" , "The river had frozen overnight." },
                    {"get", "got", "got", "получать, добираться" , "to receive something or be given something" , "Did you get anything nice for your birthday?" },
                    {"give", "gave", "given", "дать, подать, дарить" , "to provide someone with something" , "Her parents gave her a car for her birthday." },
                    {"go", "went", "gone", "идти, двигаться" , "to move or travel somewhere" , "I'd love to go to America. | When I turned round the man had gone." },
                    {"grow", "grew", "grown", "расти, вырастать" , "to develop and become bigger or taller as time passes" , "Children grow very quickly." },
                    {"hang", "hung", "hung", "вешать, развешивать, висеть" , "to fasten something so that the top part is fixed but the lower part is free to move, or to be fastened in this way" , "He hung his coat on the hook behind the door." },
                    {"have", "had", "had", "иметь, обладать" , "used with the past participle of another verb to form the present and past perfect tenses" , "I've passed my test." },
                    {"hear", "heard", "heard", "слышать, услышать" , "to be aware of a sound through your ears" , "I could hear his voice in the distance." },
                    {"hide", "hid", "hidden", "прятать, скрывать" , "to put something in a place where it cannot be seen or found" , "I hid the money in a vase." },
                    {"hit", "hit", "hit", "ударять, поражать" , " to touch something quickly and with force using your hand or an object in your hand" , "She hit him on the head with her tennis racket." },
                    {"hold", "held", "held", "держать, удерживать, задерживать" , "to have something in your hand or arms" , "He was holding a glass of wine." },
                    {"hurt", "hurt", "hurt", "ранить, причинять боль, ушибить" , "to cause someone pain or to injure them" , "Simon hurt his knee playing football." },
                    {"keep", "kept", "kept", "хранить, сохранять, поддерживать" , "to have something permanently or for the whole of a period of time" , "You can keep that dress if you like it." },
                    {"know", "knew", "known", "знать, иметь представление" , "to have knowledge or information about something in your mind" , "Andrew knows a lot about computers." },
                    {"lay", "laid", "laid", "класть, положить" , "to put something down somewhere carefully" , "She laid the baby on the bed." },
                    {"lead", "led", "led", "вести, показывать путь | руководить" , "to show someone where to go, usually by taking them to a place or by going in front of them | to be in control of a group, country, or situation" , "Casillas led his team to victory." },
                    {"leave", "left", "left", "уходить, уезжать, покидать, оставлять" , "to go away from a place or a situation, either permanently or for a temporary period | to not take something with you when you go away from a place, either intentionally or by accident" , "She'd left a note for him in the kitchen." },
                    {"lend", "lent", "lent", "одалживать, давать взаймы (в долг)" , "to give something to someone for a period of time, expecting that they will then give it back to you" , "She lent me her car for the weekend." },
                    {"let", "let", "let", "позволять, разрешать" , "to allow someone to do something, or to allow something to happen" , "Don't let the camera get wet." },
                    {"lie", "lay", "lain", "лежать" , "to be in a horizontal or flat position on a surface" , "The pen lay on the desk." },
                    {"light", "lit", "lighted", "загораться, светиться, освещать" , "to start to burn, or to make something start to burn | to produce light somewhere so that you can see things" , "Burning buildings lit up the sky." },
                    {"lose", "lost", "lost", "терять, лишаться, утрачивать" , "to not be able to find someone or something | to stop having someone or something that you had before" , "She's always losing her car keys." },
                    {"make", "made", "made", "делать, создавать, изготавливать" , "to produce or create something" , "They've made a film about her life." },
                    {"mean", "meant", "meant", "значить, иметь в виду, означать" , "to have a particular meaning | to intend to express a fact or opinion | to have a particular result" , "The red light means stop." },
                    {"meet", "met", "met", "встречать, знакомиться" , "to see and speak to someone for the first time | to come to the same place as someone else by arrangement or by chance" , "We met for coffee last Sunday." },
                    {"pay", "paid", "paid", "платить, оплачивать, рассчитываться" , "to give money to someone because you are buying something from them, or because you owe them money" , "Helen paid for the tickets." },
                    {"put", "put", "put", "ставить, помещать, класть" , " to move something to a place or position" , "She put her bag on the floor." },
                    {"read", "read", "read", "читать, прочитать" , "to look at words and understand what they mean" , "What was the last book you read?" },
                    {"ride", "rode", "ridden", "ехать верхом, кататься" , "to travel by sitting on a horse, bicycle, or motorcycle and controlling it" , "I ride my bike to work." },
                    {"ring", "rang", "rung", "звенеть, звонить" , " it makes the sound of a bell | to telephone someone" , "The phone's ringing. | I've rung for a taxi." },
                    {"rise", "rose", "risen", "восходить, подниматься, вставать, повышаться" , "to be high above something | to move up | to increase in level | " , "The balloon rose slowly into the air. | Prices rose by 10 percent. | The sun rises in the East." },
                    {"run", "ran", "run", "бежать, бегать | управлять, руководить" , "to move on your feet at a faster speed than walking | to organize or control something" , "We had to run to catch up with him. | She ran her own restaurant for five years." },
                    {"say", "said", "said", "говорить, сказать, показывать" , "to speak words | to give information in writing, numbers, or signs" , "My watch says one o'clock. | I couldn't hear what they were saying." },
                    {"see", "saw", "seen", "видеть, понимать" , "to notice people and things with your eyes | to understand something" , "I see what you mean. | Turn the light on so I can see." },
                    {"seek", "sought", "sought", "искать, разыскивать" , "to try to find or get something | attempt to find" , "it's his job to seek out new customers" },
                    {"sell", "sold", "sold", "продавать, торговать" , " to give something to someone who gives you money for it" , "He sold his guitar for $50." },
                    {"send", "sent", "sent", "посылать, отправлять, отсылать" , "to make someone go somewhere | to arrange for something to go or be taken somewhere" , "I sent him into the house to fetch some glasses." },
                    {"set", "set", "set", "устанавливать, назначать" , "to arrange a time when something will happen | to decide the level of something" , "I've set the alarm for 6.30. | The interest rate has been set at 5%." },
                    {"shake", "shook", "shaken", "трясти, встряхивать" , "to make quick, short movements from side to side or up and down, or to make something or someone do this" , "He was shaking with nerves." },
                    {"shine", "shone", "shone", "светить, сиять, освещать" , "to produce bright light | to point a light somewhere" , "The sun was shining brightly through the window." },
                    {"shoot", "shot", "shot", "стрелять" , "to fire a bullet from a gun" , "An innocent bystander was shot dead in the incident." },
                    {"show", "showed", "shown", "показывать" , "to let someone look at something" , "Show your passport to the officer." },
                    {"shut", "shut", "shut", "закрывать, запирать, затворять" , "to close something, or to become closed" , "Shut the door." },
                    {"sing", "sang", "sung", "петь, напевать" , "to make musical sounds with your voice" , "She sings in the church choir." },
                    {"sink", "sank", "sunk", "тонуть, погружаться" , "to go down or make something go down below the surface of water and not come back up" , "The Titanic sank after hitting an iceberg." },
                    {"sit", "sat", "sat", "сидеть, садиться" , "to move your body into a sitting position after you have been standing" , "She came over and sat beside him." },
                    {"sleep", "slept", "slept", "спать" , " to be in the state of rest when your eyes are closed, your body is not active, and your mind is unconscious" , "Did you sleep well?" },
                    {"speak", "spoke", "spoken", "говорить, разговаривать" , "to say something using your voice" , "There was complete silence - nobody spoke." },
                    {"spend", "spent", "spent", "тратить, расходовать, проводить (время)" , "to use money to buy or pay for something | to use time doing something or being somewhere" , "He spent 18 months working on the project." },
                    {"stand", "stood", "stood", "стоять, вставать" , "to be in a vertical position on your feet | to rise to a vertical position on your feet from sitting or lying down" , "We'd been standing for hours." },
                    {"steal", "stole", "stolen", "воровать, красть" , "to secretly take something that does not belong to you, without intending to return it" , "Burglars broke into the house and stole a computer." },
                    {"stick", "stuck", "stuck", "приклеивать" , "to become joined to something or to make something become joined to something else, usually with a substance like glue" , "Anne stuck a picture of her boyfriend on the wall." },
                    {"strike", "struck", "struck, stricken", "ударять, стукнуть" , "to hit someone or something" , "His car went out of control and struck a tree." },
                    {"swear", "swore", "sworn", "клясться | ругаться" , "to make a serious promise | to use language that people think is rude or offensive" , "He was sent home because he swore at the teacher. | I swear to tell the truth." },
                    {"sweep", "swept", "swept", "подметать" , "to clean the floor using a brush" , "She's just swept the floor." },
                    {"swim", "swam", "swum", "плавать, плыть" , "to move through water by moving your body" , "I learnt to swim when I was about 5 years old." },
                    {"swing", "swung", "swung", "качаться, размахивать" , "to move smoothly backwards and forwards, or to make something do this" , "She really swings her arms when she walks." },
                    {"take", "took", "taken", "брать, принимать (соглашаться на что-либо)" , "to get and carry something with you when you go somewhere | to accept something" , "I always take my mobile phone with me. | So, are you going to take the job?" },
                    {"teach", "taught", "taught", "учить, обучать" , "to show or explain to someone how to do something | to give lessons in a particular subject" , "She taught at Harvard University for several years." },
                    {"tear", "tore", "torn", "рвать, разрывать, отрывать" , "to pull paper, cloth, etc into pieces, or to make a hole in it by accident | it becomes damaged because it has been pulled." , "The nail had torn a hole in my skirt." },
                    {"tell", "told", "told", "рассказывать" , " to say something to someone, usually giving them information" , "He told me about his new school." },
                    {"think", "thought", "thought", "думать, полагать, размышлять" , "to consider an idea or a problem | to have an opinion about something or someone" , "Do you think it's going to rain?" },
                    {"throw", "threw", "thrown", "бросать, кидать, метать" , "to make something move through the air by pushing it out of your hand" , "He threw the book at the wall." },
                    {"understand", "understood", "understood", "понимать" , "to know the meaning of something that someone says | to know why or how something happens or works" , "I don't understand half of what he says." },
                    {"wake", "woke", "woken", "просыпаться, будить" , "to stop sleeping or to make someone else stop sleeping" , "You woke me up making so much noise." },
                    {"wear", "wore", "worn", "носить (одежду)" , "to have a piece of clothing, jewellery, etc on your body" , "I wear jeans a lot of the time." },
                    {"win", "won", "won", "победить, выиграть" , "to be successful in a war, fight, or argument | to get a prize in a game or competition" , "Protesters have won their battle to stop the road being built. | She won a gold medal at the Olympics." },
                    {"write", "wrote", "written", "писать" , "to produce words, letters, or numbers on a surface using a pen or pencil" , "Write your name at the top of the page." }
            };

            db.beginTransaction();
            ContentValues fields = new ContentValues();
            for(String[]word:importantSomeWords){
                fields.clear();
                fields.put(IrregularVerbs.Table.Infinitive_V1, word[0]);
                fields.put(IrregularVerbs.Table.PAST_SIMPLE_V2, word[1]);
                fields.put(IrregularVerbs.Table.PAST_PARTICPLE_V3, word[2]);
                fields.put(IrregularVerbs.Table.RU, word[3]);
                fields.put(IrregularVerbs.Table.ENG_VALUE, word[4]);
                fields.put(IrregularVerbs.Table.EXAMPLE, word[5]);

                db.insert(IrregularVerbs.Table.TABLE_NAME, null, fields);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }
}


