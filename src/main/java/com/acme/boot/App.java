package com.acme.boot;

import com.acme.boot.util.EmailValidator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        String email = args.length > 0 ? args[0] : "Demo.User@Acme.com";
        System.out.println( email + " valid? " + EmailValidator.isValid( email ) );
    }
}
