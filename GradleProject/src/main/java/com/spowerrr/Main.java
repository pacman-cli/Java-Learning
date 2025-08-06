package com.spowerrr;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class Main  {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello.");
//        OkHttpClient client = new OkHttpClient();
//        String url ="https://jsonplaceholder.typicode.com/todos/1";
//
//        //Http request
//        Request req = new Request.Builder()
//                .url(url)
//                .build();
//        try(Response response = client.newCall(req).execute()) { //getting response passing the request in the client's newCall
//            if(!response.isSuccessful()){
//                System.out.println("Something went wrong!");
//            }
//            System.out.println(response.body().string());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //create a retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/") //this would be the base url
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TodoService todoService =retrofit.create(TodoService.class);
        Todo t =todoService.getTodoById("1").execute().body();
        System.out.println("Todo object downloaded is : " + t.toString());
    }
}