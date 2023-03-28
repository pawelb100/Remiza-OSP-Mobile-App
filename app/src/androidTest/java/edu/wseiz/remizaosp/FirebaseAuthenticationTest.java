package edu.wseiz.remizaosp;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4.class)
public class FirebaseAuthenticationTest {
    @Test
    public void testLoginSuccessful1() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(1);
        mAuth.signInWithEmailAndPassword("michal@gmail.com", "P@ssw0rd")
                .addOnCompleteListener(task -> {
                    Assert.assertTrue(task.isSuccessful());
                    latch.countDown();
                });
        latch.await();
    }

    @Test
    public void testLoginSuccessful2() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(1);
        mAuth.signInWithEmailAndPassword("kamil@gmail.com", "P@ssw0rd")
                .addOnCompleteListener(task -> {
                    Assert.assertTrue(task.isSuccessful());
                    latch.countDown();
                });
        latch.await();
    }

    @Test
    public void testLoginSuccessful3() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(1);
        mAuth.signInWithEmailAndPassword("marek@gmail.com", "P@ssw0rd")
                .addOnCompleteListener(task -> {
                    Assert.assertTrue(task.isSuccessful());
                    latch.countDown();
                });
        latch.await();
    }

    @Test
    public void testLoginWrongPassword1() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(1);
        mAuth.signInWithEmailAndPassword("michal@gmail.com", "Wrongpassword")
                .addOnCompleteListener(task -> {
                    Assert.assertFalse(task.isSuccessful());
                    latch.countDown();
                });
        latch.await();
    }

    @Test
    public void testLoginWrongPassword2() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(1);
        mAuth.signInWithEmailAndPassword("marek@gmail.com", "Wrongpassword1234")
                .addOnCompleteListener(task -> {
                    Assert.assertFalse(task.isSuccessful());
                    latch.countDown();
                });
        latch.await();
    }

    @Test
    public void testLoginWrongPassword3() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(1);
        mAuth.signInWithEmailAndPassword("kamil@gmail.com", "Wrongpassword0")
                .addOnCompleteListener(task -> {
                    Assert.assertFalse(task.isSuccessful());
                    latch.countDown();
                });
        latch.await();
    }

    @Test
    public void testRegisterNewAccount1() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(1);
        int randomUserNumber = (int)Math.floor(Math.random() * 10000);
        String email = "testUser" + randomUserNumber + "@gmail.com";

        mAuth.createUserWithEmailAndPassword(email, "password")
                .addOnCompleteListener(task -> {
                    Assert.assertTrue(task.isSuccessful());
                    latch.countDown();
                });
        latch.await();
    }

    @Test
    public void testRegisterNewAccount2() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(1);
        int randomUserNumber = (int)Math.floor(Math.random() * 10000);
        String email = "testUser" + randomUserNumber + "@gmail.com";

        mAuth.createUserWithEmailAndPassword(email, "P@ssw0rd")
                .addOnCompleteListener(task -> {
                    Assert.assertTrue(task.isSuccessful());
                    latch.countDown();
                });
        latch.await();
    }



}
